package com.eormega.dao;

import com.eormega.pojo.Tagmap;

public interface TagmapMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tagmap record);

    int insertSelective(Tagmap record);

    Tagmap selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tagmap record);

    int updateByPrimaryKey(Tagmap record);
}