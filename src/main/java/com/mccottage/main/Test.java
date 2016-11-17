package com.mccottage.main;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;

public class Test {
	@RequestMapping("test/addData.do")
	public void insertData() {
		// 获取配置
		/*ApplicationContext context = new ClassPathXmlApplicationContext("classpath:*.xml");
		StatisticDailyMapper statisticDailyMapper = context.getBean(StatisticDailyMapper.class);
		StatisticHourlyMapper statisticHourlyMapper = context.getBean(StatisticHourlyMapper.class);*/
		List<String> requestList = Config.getInstance().getCountTypeList();
		// 获取key
		List<String> keyList = Arrays.asList(new String[]{"1","2","GPMP-0421-haRomB8Myv8oaH4zwXMaE5_CI-0420-qFfcAOfJFd69sEk4I0sCi6","CI-0421-LkkYPSIyCP6UxXrZh7YGk4_GPMP-0421-J872Vxvrb18OzsqbyF23g7"});
		// 构造插入记录
//		List<Date> randomDate = 
		for (Date date : getRadomHour("2016111513", "2016111616",20)){
			
			Random r = new Random();
			Random r2 = new Random();
			Random r3 = new Random();
			System.out.println(r.nextInt(requestList.size() - 1));
			String radomReq = requestList.get(r.nextInt(requestList.size() - 1));
			
			StatisticHourly statisticHourly = new StatisticHourly();
			statisticHourly.setType(radomReq);
			statisticHourly.setCountKey(keyList.get(r2.nextInt(keyList.size() - 1)) + "@_@" + sdf.format(date));
			statisticHourly.setCount(Long.valueOf(r3.nextInt(99999)));
			statisticHourly.setCreateTime(new Date());
			statisticHourly.setCountHour(date);
			
			statisticHourlyMapper.insert(statisticHourly);
		}
	}
	
	public static Set<Date> getRadomHour(String startHour, String endHour,int s) {
		try {
			Set<Date> randomDateSet = new HashSet<Date>();
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(sdf.parse(startHour));
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(sdf.parse(endHour));
			int size= 0;
			// 生成多少个
			while (size <= s) {
				System.out.println(sdf.parse(endHour) + " ---- " + sdf.parse(startHour));
				int d = (int)((sdf.parse(endHour).getTime() - sdf.parse(startHour).getTime())/(1000 * 60 * 60));
				Random r = new Random();
				
				System.out.println(r.nextInt(d));
				calendar1.setTime(sdf.parse(startHour));
				System.out.println("hour : === > " + calendar1.get(Calendar.HOUR_OF_DAY) + ",minutes : === >" + calendar1.get(Calendar.MINUTE) + ",toString = " + sdf.format(calendar1.getTime()));
				calendar1.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY) + r.nextInt(d));
				Date date = calendar1.getTime();
				if (date.getTime() <= calendar2.getTime().getTime()) {
					size ++;
					randomDateSet.add(date);
				};
			}
			return randomDateSet;
		} catch (Exception ex) {
			System.out.println("parse error");
		}
		
		return null;
	}
	
	
	// 测试final修饰变量
	private void testFinal() {
		final int i = 1;
		i ++; // i 被 final修饰，不能改变值，基本类型都不能改变值
		// final 修饰引用类型不能改变引用指向的对象，但是引用本身是可以改变的。
		final String s1 = "a";
		final String s2 = "b";
		s1 += "b"; // String类型的拼接会导致对象指向了新的对象，s1 引用被final修饰，因此报错
		
		// StringBuffer 字符串的append方法拼接不会指向新的对象,因此不会报错
		final StringBuffer sb = new StringBuffer("c");
		sb.append("d");
		
		final int a = 1;
		System.out.println();
	}
}
