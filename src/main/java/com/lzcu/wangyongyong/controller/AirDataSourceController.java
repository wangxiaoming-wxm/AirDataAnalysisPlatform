package com.lzcu.wangyongyong.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzcu.wangyongyong.common.enums.Constant;
import com.lzcu.wangyongyong.common.utils.AirDataCaptureUtil;
import com.lzcu.wangyongyong.common.utils.ExampleData;
import com.lzcu.wangyongyong.common.utils.JsonToBeanUtil;
import com.lzcu.wangyongyong.mapper.AirDataSourceMapper;
import com.lzcu.wangyongyong.model.AirDataSource;
import com.lzcu.wangyongyong.service.AirDataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * 航空数据原始表;(air_data_source)表控制层
 * @author : 王勇勇
 * @date : 2023-2-28
 */
@Api(tags = "航空数据原始表对象功能接口")
@RestController
@RequestMapping("/airDataSource")
@Slf4j
public class AirDataSourceController{
    @Autowired
    private AirDataSourceService airDataSourceService;
    @Autowired
    private AirDataSourceMapper airDataSourceMapper;

    /**
     * 新增数据
     * @param dayId 请求参数，"dayId":"2023-02-28"
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(@RequestParam("dayId") String  dayId){
        log.info("接收到的请求参数=【{}】",dayId);
        List<String> fromAirportList = airDataSourceService.getFromAirportCode3();
        List<String> toAirportList = airDataSourceService.getToAirportCode3();
        fromAirportList.forEach(fromAirport -> toAirportList.forEach(toAirport -> {
            //每次数据抓取前随机等待5秒至20秒
            try {
                Thread.sleep((new Random()).nextInt(Constant.MAX)%(Constant.MIN) + Constant.MIN);
            } catch (InterruptedException e) {
                log.error("随机睡眠时间出错，请检查！");
                throw new RuntimeException(e);
            }
            JSONObject airLineDataJson = AirDataCaptureUtil.getAirLineResp(dayId,fromAirport,toAirport);
            String data = airLineDataJson.getString("data");
            JSONArray jsonArray = JSONArray.parseArray(data);
            //JSONArray jsonArray = JSONArray.parseArray(ExampleData.exampleData); //测试可用此方法
            List<AirDataSource> airDataSourceList = new ArrayList<>();
            jsonArray.forEach(x -> {
                JSONObject jsonObject = (JSONObject)x;
                log.info("获取到的航空数据=【{}】",jsonObject);
                AirDataSource airDataSource = JsonToBeanUtil.toAirDataSource(jsonObject);
                airDataSourceList.add(airDataSource);
            });
            if(null != airDataSourceList && airDataSourceList.size() > 0){
                airDataSourceService.saveBatch(airDataSourceList);
            }
        }));

        return "已完成"+ dayId +"航空数据采集";
    }
}