package com.songtaste.dao;

import com.songtaste.entity.MusicTypeRelation;
import com.songtaste.entity.MusicTypeRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MusicTypeRelationMapper {
    int countByExample(MusicTypeRelationExample example);

    int deleteByExample(MusicTypeRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MusicTypeRelation record);

    int insertSelective(MusicTypeRelation record);

    List<MusicTypeRelation> selectByExample(MusicTypeRelationExample example);

    MusicTypeRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MusicTypeRelation record, @Param("example") MusicTypeRelationExample example);

    int updateByExample(@Param("record") MusicTypeRelation record, @Param("example") MusicTypeRelationExample example);

    int updateByPrimaryKeySelective(MusicTypeRelation record);

    int updateByPrimaryKey(MusicTypeRelation record);
}