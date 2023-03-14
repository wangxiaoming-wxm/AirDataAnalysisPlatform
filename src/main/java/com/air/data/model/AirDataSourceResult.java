package com.air.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 航空数据原始表;
 * @author : 王勇勇
 * @date : 2023-3-14
 */
@ApiModel(value = "航空数据原始表",description = "")
@TableName("air_data_source_result")
@Data
public class AirDataSourceResult extends Model<AirDataSourceResult> implements Serializable {

    /** 主键 */
    @ApiModelProperty(name = "主键",notes = "")
    @TableId(type = IdType.AUTO)
    private Integer id ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String airlinelogo ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String airlineshortcompany ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arractcross ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arracttime ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arrairport ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arrcode ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arrontimerate ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arrplancross ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arrplantime ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String arrterminal ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String checkintable ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String checkintablewidth ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depactcross ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depacttime ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depairport ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depcode ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depplancross ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depplantime ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String depterminal ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String flightno ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String flightstate ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String localdate ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String mainflightno ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String shareflag ;
    /**  */
    @ApiModelProperty(name = "",notes = "")
    private String statecolor ;
}