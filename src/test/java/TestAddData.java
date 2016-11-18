package test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gexin.platform.gpmp.dao.AdLocationMapper;
import com.gexin.platform.gpmp.dao.AdTaskCreativeInstanceMapper;
import com.gexin.platform.gpmp.dao.LocationStatisticDailyMapper;
import com.gexin.platform.gpmp.dao.LocationStatisticHourlyMapper;
import com.gexin.platform.gpmp.dao.TaskCiStatisticDailyMapper;
import com.gexin.platform.gpmp.dao.TaskCiStatisticHourlyMapper;
import com.gexin.platform.gpmp.dao.relate.LocationStatisticDailyGroupMapper;
import com.gexin.platform.gpmp.dao.relate.TaskStatisticDailyGroupMapper;
import com.gexin.platform.gpmp.model.AdLocation;
import com.gexin.platform.gpmp.model.AdLocationExample;
import com.gexin.platform.gpmp.model.AdTaskCreativeInstance;
import com.gexin.platform.gpmp.model.AdTaskCreativeInstanceExample;
import com.gexin.platform.gpmp.model.LocationStatisticDaily;
import com.gexin.platform.gpmp.model.LocationStatisticHourly;
import com.gexin.platform.gpmp.model.TaskCiStatisticDaily;
import com.gexin.platform.gpmp.model.TaskCiStatisticHourly;
import com.gexin.rp.base.log.Log;
import com.gexin.rp.cluster.factory.ClusterFactory;

import scala.util.Random;

// gpmp造数据
public class TestAddData {

	public static ApplicationContext context = null;

	public static SimpleDateFormat sdf_hour = new SimpleDateFormat("yyyyMMddHH");

	public static SimpleDateFormat sdf_day = new SimpleDateFormat("yyyyMMdd");

	static {
		System.setProperty("zkHost", "192.168.11.2");
		System.setProperty("myName", "gpmp-1");
		System.setProperty("myRole", "gpmp");
		Log.loadLevel();
		ClusterFactory.getCluster().loadStatic();
		context = new ClassPathXmlApplicationContext("classpath:spring/*.xml");
	}

	public static void main(String[] args) {
//		addTaskCiHour("2016110811", "2016111811",100);
//		syncTaskCiHourDataToDay("20161110", "20161118");
		
//		addLocationHour("2016111011", "2016111814",100);
		syncLocationHourDataToDay("20161110", "20161118");
		 
		// addTaskCiDaily("20161011", "20161118", 100);
		// addLocationDaily("20161011", "20161118", 100);
	}
	
	public static void syncTaskCiHourDataToDay(String startDay, String endDay){
		try {
			TaskStatisticDailyGroupMapper taskStatisticDailyGroupMapper = (TaskStatisticDailyGroupMapper) context.getBean("taskStatisticDailyGroupMapper");
			TaskCiStatisticDailyMapper taskCiStatisticDailyMapper = (TaskCiStatisticDailyMapper)context.getBean("taskCiStatisticDailyMapper");
			List<TaskCiStatisticDaily> TaskCiStatisticDailyList = taskStatisticDailyGroupMapper.getStatisticDailyGroupCountByDate(sdf_day.parse(startDay), sdf_day.parse(endDay));
			for (TaskCiStatisticDaily taskCiStatisticDaily : TaskCiStatisticDailyList) {
				taskCiStatisticDailyMapper.insert(taskCiStatisticDaily);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void syncLocationHourDataToDay(String startDay, String endDay) {
		try {
			LocationStatisticDailyGroupMapper locationStatisticDailyGroupMapper = (LocationStatisticDailyGroupMapper) context.getBean("locationStatisticDailyGroupMapper");
			LocationStatisticDailyMapper locationStatisticDailyMapper = (LocationStatisticDailyMapper) context.getBean("locationStatisticDailyMapper");
			List<LocationStatisticDaily> locationStatisticDailyList = locationStatisticDailyGroupMapper.getStatisticDailyGroupCountByDate(sdf_day.parse(startDay), sdf_day.parse(endDay));
			for (LocationStatisticDaily locationStatisticDaily : locationStatisticDailyList) {
				locationStatisticDailyMapper.insert(locationStatisticDaily);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addLocationDaily(String startDay, String endDay, int number) {
		try {
			AdLocationMapper adLocationMapper = (AdLocationMapper) context.getBean("adLocationMapper");
			LocationStatisticDailyMapper locationStatisticDailyMapper = (LocationStatisticDailyMapper) context
					.getBean("locationStatisticDailyMapper");
			// 查出所有的广告位
			List<AdLocation> allLocationList = adLocationMapper.selectByExample(new AdLocationExample());
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(sdf_day.parse(startDay));

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(sdf_day.parse(endDay));

			// 相差天数
			int d = (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
			Random r = new Random();
			for (int i = 0; i < number; i++) {
				// 随即获取一个adTaskCreativeInstance
				AdLocation location = allLocationList.get(r.nextInt(allLocationList.size())); // 随即获取一个广告位
				LocationStatisticDaily locationStatisticDaily = new LocationStatisticDaily();
				locationStatisticDaily.setBidNum(Long.valueOf(r.nextInt(99999)));
				locationStatisticDaily.setCost(new BigDecimal(r.nextInt(99999)));
				locationStatisticDaily.setCreateTime(new Date());
				locationStatisticDaily.setLocationId(location.getId());
				locationStatisticDaily.setMediaId(location.getMediaId());
				locationStatisticDaily.setReqNum(Long.valueOf(r.nextInt(99999)));
				locationStatisticDaily.setWinNum(Long.valueOf(r.nextInt(99999)));
				Calendar calendar3 = Calendar.getInstance();
				int index = r.nextInt(d);
				System.out.println("index :" + index);
				calendar3.set(Calendar.DAY_OF_YEAR, calendar1.get(Calendar.DAY_OF_YEAR) + index);
				locationStatisticDaily.setCountDay(sdf_day.parse(sdf_day.format(calendar3.getTime())));
				locationStatisticDailyMapper.insert(locationStatisticDaily);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addLocationHour(String startHour, String endHour, int number) {
		try {
			AdLocationMapper adLocationMapper = (AdLocationMapper) context.getBean("adLocationMapper");
			LocationStatisticHourlyMapper locationStatisticHourlyMapper = (LocationStatisticHourlyMapper) context
					.getBean("locationStatisticHourlyMapper");
			// 查出所有的广告位
			List<AdLocation> allLocationList = adLocationMapper.selectByExample(new AdLocationExample());
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(sdf_hour.parse(startHour));

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(sdf_hour.parse(endHour));

			// 相差小时
			int d = (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60 * 60));
			Random r = new Random();
			for (int i = 0; i < number; i++) {
				// 随即获取一个adTaskCreativeInstance
				AdLocation location = allLocationList.get(r.nextInt(allLocationList.size())); // 随即获取一个广告位
				LocationStatisticHourly locationStatisticHourly = new LocationStatisticHourly();
				locationStatisticHourly.setBidNum(Long.valueOf(r.nextInt(99999)));
				locationStatisticHourly.setCost(new BigDecimal(r.nextInt(99999)));
				locationStatisticHourly.setCreateTime(new Date());
				locationStatisticHourly.setLocationId(location.getId());
				locationStatisticHourly.setMediaId(location.getMediaId());
				locationStatisticHourly.setReqNum(Long.valueOf(r.nextInt(99999)));
				locationStatisticHourly.setWinNum(Long.valueOf(r.nextInt(99999)));
				Calendar calendar3 = Calendar.getInstance();
				calendar3.setTime(sdf_hour.parse(startHour));
				int index = r.nextInt(d);
				System.out.println("index :" + index);
				calendar3.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY) + index);
				locationStatisticHourly.setCountHour(sdf_hour.parse(sdf_hour.format(calendar3.getTime())));
				locationStatisticHourlyMapper.insert(locationStatisticHourly);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addTaskCiHour(String startHour, String endHour, int number) {
		try {
			AdTaskCreativeInstanceMapper adTaskCreativeInstanceMapper = (AdTaskCreativeInstanceMapper) context
					.getBean("adTaskCreativeInstanceMapper");
			List<AdTaskCreativeInstance> adTaskCreativeInstanceList = adTaskCreativeInstanceMapper
					.selectByExample(new AdTaskCreativeInstanceExample());

			TaskCiStatisticHourlyMapper taskCiStatisticHourlyMapper = (TaskCiStatisticHourlyMapper) context
					.getBean("taskCiStatisticHourlyMapper");
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(sdf_hour.parse(startHour));

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(sdf_hour.parse(endHour));

			// 相差小时
			int d = (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60 * 60));
			Random r = new Random();
			for (int i = 0; i < number; i++) {
				TaskCiStatisticHourly taskCiStatisticHourly = new TaskCiStatisticHourly();
				// 随即获取一个adTaskCreativeInstance
				AdTaskCreativeInstance adTaskCreativeInstance = adTaskCreativeInstanceList
						.get(r.nextInt(adTaskCreativeInstanceList.size()));
				taskCiStatisticHourly.setTaskId(adTaskCreativeInstance.getTaskId());
				taskCiStatisticHourly.setCreativeInstanceId(adTaskCreativeInstance.getInstanceId());
				taskCiStatisticHourly.setBidNum(Long.valueOf(r.nextInt(99999)));
				taskCiStatisticHourly.setClickNum(Long.valueOf(r.nextInt(9999)));
				taskCiStatisticHourly.setCost(new BigDecimal(r.nextInt(99999)));
				taskCiStatisticHourly.setCreateTime(new Date());
				taskCiStatisticHourly.setExpNum(Long.valueOf(r.nextInt(99999)));
				taskCiStatisticHourly.setUv(Long.valueOf(r.nextInt(9999)));
				taskCiStatisticHourly.setReqNum(Long.valueOf(r.nextInt(99999)));
				taskCiStatisticHourly.setWinNum(Long.valueOf(r.nextInt(9999)));
				Calendar calendar3 = Calendar.getInstance();
				int index = r.nextInt(d);
				System.out.println("index :" + index);
				calendar3.setTime(sdf_hour.parse(startHour));
				calendar3.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY) + index);
				System.out.println("add : " + sdf_hour.format(calendar3.getTime()));
				taskCiStatisticHourly.setCountHour(sdf_hour.parse(sdf_hour.format(calendar3.getTime())));

				taskCiStatisticHourlyMapper.insert(taskCiStatisticHourly);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addTaskCiDaily(String startDay, String endDay, int number) {
		try {
			AdTaskCreativeInstanceMapper adTaskCreativeInstanceMapper = (AdTaskCreativeInstanceMapper) context
					.getBean("adTaskCreativeInstanceMapper");
			List<AdTaskCreativeInstance> adTaskCreativeInstanceList = adTaskCreativeInstanceMapper
					.selectByExample(new AdTaskCreativeInstanceExample());

			TaskCiStatisticDailyMapper taskCiStatisticHourlyMapper = (TaskCiStatisticDailyMapper) context
					.getBean("taskCiStatisticDailyMapper");
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(sdf_day.parse(startDay));

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(sdf_day.parse(endDay));

			// 相差多少天
			int d = (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
			Random r = new Random();
			for (int i = 0; i < number; i++) {
				TaskCiStatisticDaily taskCiStatisticDaily = new TaskCiStatisticDaily();
				// 随即获取一个adTaskCreativeInstance
				AdTaskCreativeInstance adTaskCreativeInstance = adTaskCreativeInstanceList
						.get(r.nextInt(adTaskCreativeInstanceList.size()));
				taskCiStatisticDaily.setTaskId(adTaskCreativeInstance.getTaskId());
				taskCiStatisticDaily.setCreativeInstanceId(adTaskCreativeInstance.getInstanceId());
				taskCiStatisticDaily.setBidNum(Long.valueOf(r.nextInt(99999)));
				taskCiStatisticDaily.setClickNum(Long.valueOf(r.nextInt(9999)));
				taskCiStatisticDaily.setCost(new BigDecimal(r.nextInt(99999)));
				taskCiStatisticDaily.setCreateTime(new Date());
				taskCiStatisticDaily.setExpNum(Long.valueOf(r.nextInt(99999)));
				taskCiStatisticDaily.setUv(Long.valueOf(r.nextInt(9999)));
				taskCiStatisticDaily.setReqNum(Long.valueOf(r.nextInt(99999)));
				taskCiStatisticDaily.setWinNum(Long.valueOf(r.nextInt(9999)));
				Calendar calendar3 = Calendar.getInstance();
				calendar3.set(Calendar.DAY_OF_YEAR, calendar1.get(Calendar.DAY_OF_YEAR) + r.nextInt(d));
				taskCiStatisticDaily.setCountDay(sdf_day.parse(sdf_day.format(calendar3.getTime())));

				taskCiStatisticHourlyMapper.insert(taskCiStatisticDaily);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
