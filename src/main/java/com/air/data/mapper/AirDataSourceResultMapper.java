package com.air.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.air.data.model.AirDataSourceResult;
import org.apache.ibatis.annotations.Mapper;

/**
 * 航空数据原始表接口mapper;
 * @author : 王勇勇
 * @date : 2023-2-28
 */
@Mapper
public interface AirDataSourceResultMapper extends BaseMapper<AirDataSourceResult>{
}