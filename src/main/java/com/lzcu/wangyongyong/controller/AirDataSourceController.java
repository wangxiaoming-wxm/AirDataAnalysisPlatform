package com.lzcu.wangyongyong.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzcu.wangyongyong.common.enums.Constant;
import com.lzcu.wangyongyong.common.utils.AirDataCaptureUtil;
import com.lzcu.wangyongyong.common.utils.ExampleData;
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
     * @param params 请求参数，{"dayId":"2023-02-28","fromAirport":"PEK","toAirport":"CAN"}
     * @return 实例对象
     */
    @ApiOperation("新增数据")
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public Boolean add(@RequestBody JSONObject params){

        log.info("接收到的请求参数=【{}】",params);
        String dayId = params.getString("dayId");
        String fromAirport = params.getString("fromAirport");
        String toAirport = params.getString("toAirport");

        //每次数据抓取前随机等待2秒至6秒
        try {
            Thread.sleep((new Random()).nextInt(Constant.MAX)%(Constant.MIN) + Constant.MIN);
        } catch (InterruptedException e) {
            log.error("随机睡眠时间出错，请检查！");
            throw new RuntimeException(e);
        }
        JSONObject airLineDataJson = null;
        airLineDataJson = AirDataCaptureUtil.getAirLineResp(dayId,fromAirport,toAirport);
        int status = airLineDataJson.getInteger("status");
        String msg = airLineDataJson.getString("msg");
        String data = airLineDataJson.getString("data");

        JSONArray jsonArray = JSONArray.parseArray(data);
        List<AirDataSource> airDataSourceList = new ArrayList<>();
        jsonArray.forEach(x -> {
            AirDataSource airDataSource = new AirDataSource();
            JSONObject jsonObject = (JSONObject)x;
            log.info("获取到的航空数据=【{}】",jsonObject);
            airDataSource.setAirlinelogo(jsonObject.getString("airlineLogo"));
            airDataSource.setAirlineshortcompany(jsonObject.getString("airlineShortCompany"));
            airDataSource.setArractcross(jsonObject.getString("arrActCross"));
            airDataSource.setArracttime(jsonObject.getString("arrActTime"));
            airDataSource.setArrairport(jsonObject.getString("arrAirport"));
            airDataSource.setArrcode(jsonObject.getString("arrCode"));
            airDataSource.setArrontimerate(jsonObject.getString("arrOntimeRate"));
            airDataSource.setArrplancross(jsonObject.getString("arrPlanCross"));
            airDataSource.setArrplantime(jsonObject.getString("arrPlanTime"));
            airDataSource.setArrterminal(jsonObject.getString("arrTerminal"));
            airDataSource.setCheckintable(jsonObject.getString("checkInTable"));
            airDataSource.setCheckintablewidth(jsonObject.getString("checkInTableWidth"));
            airDataSource.setDepactcross(jsonObject.getString("depActCross"));
            airDataSource.setDepacttime(jsonObject.getString("depActTime"));
            airDataSource.setDepairport(jsonObject.getString("depAirport"));
            airDataSource.setDepcode(jsonObject.getString("depCode"));
            airDataSource.setDepplancross(jsonObject.getString("depPlanCross"));
            airDataSource.setDepplantime(jsonObject.getString("depPlanTime"));
            airDataSource.setDepterminal(jsonObject.getString("depTerminal"));
            airDataSource.setFlightno(jsonObject.getString("flightNo"));
            airDataSource.setFlightstate(jsonObject.getString("flightState"));
            airDataSource.setLocaldate(jsonObject.getString("localDate"));
            airDataSource.setMainflightno(jsonObject.getString("mainFlightNo"));
            airDataSource.setShareflag(jsonObject.getString("shareFlag"));
            airDataSource.setStatecolor(jsonObject.getString("stateColor"));
            airDataSourceList.add(airDataSource);
        });
        Boolean isWrittenMysql = airDataSourceService.saveBatch(airDataSourceList);
        if(isWrittenMysql){
            log.info("今日获取的航空数据写入MySQL已完成！");
        }else {
            log.error("今日获取的航空数据写入MySQL失败，请检查！");
        }
        return    isWrittenMysql;
    }

}