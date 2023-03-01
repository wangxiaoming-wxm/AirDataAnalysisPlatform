package com.lzcu.wangyongyong.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzcu.wangyongyong.model.AirDataSource;

import java.util.List;

/**
 * 航空数据原始表;(air_data_source)表服务接口
 * @author : http://www.chiner.pro
 * @date : 2023-2-28
 */
public interface AirDataSourceService extends IService<AirDataSource>{

    List<String> getFromAirportCode3();
    List<String> getToAirportCode3();
}