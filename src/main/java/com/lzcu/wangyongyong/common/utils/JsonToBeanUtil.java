package com.lzcu.wangyongyong.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.lzcu.wangyongyong.model.AirDataSource;

public class JsonToBeanUtil {
    public static AirDataSource toAirDataSource (JSONObject jsonObject) {
        AirDataSource airDataSource = new AirDataSource();
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
        return airDataSource;
    }
}
