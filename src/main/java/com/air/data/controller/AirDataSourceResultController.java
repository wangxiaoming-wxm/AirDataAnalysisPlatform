package com.air.data.controller;

import com.air.data.common.utils.FileUtil;
import com.air.data.mapper.AirDataSourceResultMapper;
import com.air.data.model.AirDataSourceResult;
import com.air.data.service.AirDataSourceResultService;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.param.Operator;
import com.ejlchina.searcher.util.MapUtils;
import com.air.data.common.utils.ImageDateParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 航空数据原始表;(air_data_source)表控制层
 * @author : 王勇勇
 * @date : 2023-2-28
 */
@Api(tags = "航空数据原始表对象功能接口")
@RestController
@RequestMapping("/images")
@Slf4j
public class AirDataSourceResultController {
    @Autowired
    private AirDataSourceResultService airDataSourceResultService;
    @Autowired
    private AirDataSourceResultMapper airDataSourceResultMapper;
    @Autowired
    BeanSearcher beanSearcher;
    /**
     * 图片识别
     * @param dayId 请求参数，"dayId":"2023-02-28"
     * @return 实例对象
     */
    @ApiOperation("图片识别")
    @RequestMapping(value = "/parser",method = RequestMethod.GET)
    public String imgParser(@RequestParam("dayId") String  dayId){
        log.info("接收到的请求参数=【{}】",dayId);
        Map<String, Object> params = MapUtils.builder()
                .field(AirDataSourceResult::getLocaldate).op(Operator.Equal)
                .build();
        //查询数据库结果
        List<AirDataSourceResult> airDataList = beanSearcher.searchAll(AirDataSourceResult.class, params);
        airDataList.stream().filter(x ->
                        null != x
                        && x.getArracttime().contains("http")
                        && x.getDepacttime().contains("http")).collect(Collectors.toList())
                .forEach(x -> {
            String arrActTimeUrl = FileUtil.getNetUrlHttp(x.getArracttime());
            String depActTimeUrl = FileUtil.getNetUrlHttp(x.getDepacttime());
            String arrActTime = ImageDateParser.getImgText(arrActTimeUrl);
            String depActTime = ImageDateParser.getImgText(depActTimeUrl);
            log.info("实际起飞时间=【{}】,实际落地时间=【{}】",arrActTime,depActTime);
            x.setArracttime(arrActTime);
            x.setDepacttime(depActTime);
            airDataSourceResultMapper.updateById(x);
        });
        return "【" + dayId +  "】" + "OCR识别图片中实际起飞及降落时间，已完成识别并入库";
    }
}