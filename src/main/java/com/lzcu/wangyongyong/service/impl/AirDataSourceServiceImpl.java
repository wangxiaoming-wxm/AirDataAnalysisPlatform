package com.lzcu.wangyongyong.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.param.Operator;
import com.ejlchina.searcher.util.MapUtils;
import com.lzcu.wangyongyong.common.enums.Constant;
import com.lzcu.wangyongyong.mapper.AirDataSourceMapper;
import com.lzcu.wangyongyong.model.AirDataSource;
import com.lzcu.wangyongyong.model.DimAirport;
import com.lzcu.wangyongyong.service.AirDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 航空数据原始表;(air_data_source)表服务实现类
 * @author : http://www.chiner.pro
 * @date : 2023-2-28
 */
@Service
@Slf4j
public class AirDataSourceServiceImpl extends ServiceImpl<AirDataSourceMapper, AirDataSource> implements AirDataSourceService {
    @Autowired
    private BeanSearcher beanSearcher;
    @Override
    public List<String> getFromAirportCode3() {
        //构造查询条件，从北京、上海、广州、重庆出发
        Map<String, Object> params = MapUtils.builder()
                .field(DimAirport::getAirportCode3, Constant.BEIJING,Constant.SHANGHAI,Constant.GUANGZHOU,Constant.CHONGQING).op(Operator.InList)
                .build();
        //查询数据库结果
        List<DimAirport> list = beanSearcher.searchAll(DimAirport.class, params);

        List<String> airportList = new ArrayList<>();
        list.forEach(x -> airportList.add(x.getAirportCode3()));
        return airportList;
    }

    @Override
    public List<String> getToAirportCode3() {
        //构造查询条件，到达兰州、西安、杭州、武汉
        Map<String, Object> params = MapUtils.builder()
                .field(DimAirport::getAirportCode3, Constant.XIAN,Constant.HANGZHOU,Constant.LANZHOU,Constant.WUHAN).op(Operator.InList)
                .build();
        //查询数据库结果
        List<DimAirport> list = beanSearcher.searchAll(DimAirport.class, params);

        List<String> airportList = new ArrayList<>();
        list.forEach(x -> airportList.add(x.getAirportCode3()));
        return airportList;
    }
}