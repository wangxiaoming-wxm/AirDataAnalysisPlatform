package com.air.data.model;

import com.ejlchina.searcher.bean.DbField;
import com.ejlchina.searcher.bean.SearchBean;
import lombok.Data;

@Data
@SearchBean(
        tables = "dim_airport t1",
        //where = "t1.airport_code_3 in ('PEK','CAN','SHA','CKG') ",
        autoMapTo = "t1"
)
public class DimAirport {
    /**
     * 机场所在城市
     */
    @DbField("t1.city_name")
    private String cityName;

    /**
     * 机场代码
     */
    @DbField("t1.airport_code_3")
    private String airportCode3;

    /**
     * 机场名称
     */
    @DbField("t1.airport_name_zh")
    private String airportNameZh;
}
