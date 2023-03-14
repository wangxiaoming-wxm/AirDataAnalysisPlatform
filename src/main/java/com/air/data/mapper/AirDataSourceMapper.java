package com.air.data.mapper;

import com.air.data.model.AirDataSource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 航空数据原始表接口mapper;
 * @author : 王勇勇
 * @date : 2023-2-28
 */
@Mapper
public interface AirDataSourceMapper  extends BaseMapper<AirDataSource>{
}