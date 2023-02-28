package com.lzcu.wangyongyong.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 航空数据原始表;
 * @author : 王勇勇
 * @date : 2023-2-28
 */
@ApiModel(value = "航空数据原始表",description = "")
@TableName("air_data_source")
@Data
public class AirDataSource extends Model<AirDataSource> implements Serializable {

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

    @Override
    public AirDataSource clone() {
        try {
            AirDataSource clone = (AirDataSource) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAirlinelogo() {
        return airlinelogo;
    }

    public void setAirlinelogo(String airlinelogo) {
        this.airlinelogo = airlinelogo;
    }

    public String getAirlineshortcompany() {
        return airlineshortcompany;
    }

    public void setAirlineshortcompany(String airlineshortcompany) {
        this.airlineshortcompany = airlineshortcompany;
    }

    public String getArractcross() {
        return arractcross;
    }

    public void setArractcross(String arractcross) {
        this.arractcross = arractcross;
    }

    public String getArracttime() {
        return arracttime;
    }

    public void setArracttime(String arracttime) {
        this.arracttime = arracttime;
    }

    public String getArrairport() {
        return arrairport;
    }

    public void setArrairport(String arrairport) {
        this.arrairport = arrairport;
    }

    public String getArrcode() {
        return arrcode;
    }

    public void setArrcode(String arrcode) {
        this.arrcode = arrcode;
    }

    public String getArrontimerate() {
        return arrontimerate;
    }

    public void setArrontimerate(String arrontimerate) {
        this.arrontimerate = arrontimerate;
    }

    public String getArrplancross() {
        return arrplancross;
    }

    public void setArrplancross(String arrplancross) {
        this.arrplancross = arrplancross;
    }

    public String getArrplantime() {
        return arrplantime;
    }

    public void setArrplantime(String arrplantime) {
        this.arrplantime = arrplantime;
    }

    public String getArrterminal() {
        return arrterminal;
    }

    public void setArrterminal(String arrterminal) {
        this.arrterminal = arrterminal;
    }

    public String getCheckintable() {
        return checkintable;
    }

    public void setCheckintable(String checkintable) {
        this.checkintable = checkintable;
    }

    public String getCheckintablewidth() {
        return checkintablewidth;
    }

    public void setCheckintablewidth(String checkintablewidth) {
        this.checkintablewidth = checkintablewidth;
    }

    public String getDepactcross() {
        return depactcross;
    }

    public void setDepactcross(String depactcross) {
        this.depactcross = depactcross;
    }

    public String getDepacttime() {
        return depacttime;
    }

    public void setDepacttime(String depacttime) {
        this.depacttime = depacttime;
    }

    public String getDepairport() {
        return depairport;
    }

    public void setDepairport(String depairport) {
        this.depairport = depairport;
    }

    public String getDepcode() {
        return depcode;
    }

    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

    public String getDepplancross() {
        return depplancross;
    }

    public void setDepplancross(String depplancross) {
        this.depplancross = depplancross;
    }

    public String getDepplantime() {
        return depplantime;
    }

    public void setDepplantime(String depplantime) {
        this.depplantime = depplantime;
    }

    public String getDepterminal() {
        return depterminal;
    }

    public void setDepterminal(String depterminal) {
        this.depterminal = depterminal;
    }

    public String getFlightno() {
        return flightno;
    }

    public void setFlightno(String flightno) {
        this.flightno = flightno;
    }

    public String getFlightstate() {
        return flightstate;
    }

    public void setFlightstate(String flightstate) {
        this.flightstate = flightstate;
    }

    public String getLocaldate() {
        return localdate;
    }

    public void setLocaldate(String localdate) {
        this.localdate = localdate;
    }

    public String getMainflightno() {
        return mainflightno;
    }

    public void setMainflightno(String mainflightno) {
        this.mainflightno = mainflightno;
    }

    public String getShareflag() {
        return shareflag;
    }

    public void setShareflag(String shareflag) {
        this.shareflag = shareflag;
    }

    public String getStatecolor() {
        return statecolor;
    }

    public void setStatecolor(String statecolor) {
        this.statecolor = statecolor;
    }
}