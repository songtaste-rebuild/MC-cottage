package com.songtaste.dao;

import com.songtaste.entity.MusicAlbumRelation;
import com.songtaste.entity.MusicAlbumRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MusicAlbumRelationMapper {
    int countByExample(MusicAlbumRelationExample example);

    int deleteByExample(MusicAlbumRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MusicAlbumRelation record);

    int insertSelective(MusicAlbumRelation record);

    List<MusicAlbumRelation> selectByExample(MusicAlbumRelationExample example);

    MusicAlbumRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MusicAlbumRelation record, @Param("example") MusicAlbumRelationExample example);

    int updateByExample(@Param("record") MusicAlbumRelation record, @Param("example") MusicAlbumRelationExample example);

    int updateByPrimaryKeySelective(MusicAlbumRelation record);

    int updateByPrimaryKey(MusicAlbumRelation record);
}