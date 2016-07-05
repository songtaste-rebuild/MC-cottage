package com.mccottage.dao;

import com.mccottage.entity.MusicType;
import com.mccottage.entity.MusicTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MusicTypeMapper {
    int countByExample(MusicTypeExample example);

    int deleteByExample(MusicTypeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MusicType record);

    int insertSelective(MusicType record);

    List<MusicType> selectByExample(MusicTypeExample example);

    MusicType selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MusicType record, @Param("example") MusicTypeExample example);

    int updateByExample(@Param("record") MusicType record, @Param("example") MusicTypeExample example);

    int updateByPrimaryKeySelective(MusicType record);

    int updateByPrimaryKey(MusicType record);
}