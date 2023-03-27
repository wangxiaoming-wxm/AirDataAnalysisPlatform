package com.air.data.common.utils;

import org.apache.spark.sql.RuntimeConfig;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

/**
 * atour-report
 *
 * @author 郭靖/fangxue.zhang
 * @date 2019-07-04
 * 用来处理spark session相关的逻辑.
 */
public class SparkSessionUtil {

    /**
     * 默认的初始化spark session 的工具类.
     * @param appName
     * @return
     */
    public static SparkSession getDefaultSession(String appName) {
        SparkSession ss = SparkSession.builder().appName(appName)
                .enableHiveSupport()
                .config( "spark.sql.warehouse.dir", "hdfs://master:9820/tmp/spark-warehouse" )
                .getOrCreate();
        ss.conf().set( "spark.sql.broadcastTimeout", 4 * 60 * 60 + "" );
        ss.conf().set( "hive.exec.dynamic.partition.mode", "nonstrict" );
        //使用Kryo序列化库
        ss.conf().set( "spark.serializer", "org.apache.spark.serializer.KryoSerializer" );
        ss.conf().set( "spark.kryoserializer.buffer.max", "102400" );
        ss.conf().set( "spark.kryoserializer.buffer", "1024" );
        ss.conf().set( "spark.shuffle.file.buffer", "64" );
        ss.conf().set( "spark.sql.crossJoin.enabled", "true" );
        return ss;
    }

    /**
     * 获取自定义的SparkSession
     * @param appName            应用名称
     * @param broadcastTimeout   广播超时时间
     * @param parallelism        并行度
     * @param partitions         分区数量
     * @return
     */
    public static SparkSession getCustomSession(String appName, int broadcastTimeout, int parallelism, int partitions) {
        SparkSession sparkSession = SparkSession.builder()
                .appName(appName)
                .enableHiveSupport()
                .config("spark.sql.warehouse.dir", "hdfs://nameservice1//tmp/spark-warehouse")
                .getOrCreate();

        RuntimeConfig config = sparkSession.conf();
        config.set("spark.sql.broadcastTimeout", broadcastTimeout);
        config.set("spark.default.parallelism", parallelism);
        config.set("spark.sql.shuffle.partitions", partitions);
        config.set("hive.exec.dynamic.partition.mode", "nonstrict");
        // 使用Kryo序列化库
        config.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        // 单位M
        config.set("spark.kryoserializer.buffer.max", "102400");
        // 单位K
        config.set("spark.kryoserializer.buffer", "1024");
        config.set("spark.shuffle.file.buffer", "64");
        config.set("spark.sql.crossJoin.enabled", "true");
        return sparkSession;
    }

    /**
     * 获取操作ES的SparkSession
     */
    public static SparkSession getEsSession(String appName, Map<String, String> esConfig) {
        SparkSession sparkSession = SparkSession.builder()
                .appName(appName)
                .enableHiveSupport()
                .config("spark.sql.warehouse.dir", "hdfs://nameservice1//tmp/spark-warehouse")
                .config("spark.sql.crossJoin.enabled", "true")
                .config("es.index.auto.create", "true")
                .config("es.nodes", esConfig.get("hosts"))
                .config("es.port", "9200")
                .config("es.net.http.auth.user", esConfig.get("user"))
                .config("es.net.http.auth.pass", esConfig.get("pass"))
                .getOrCreate();
        return sparkSession;
    }

    /**
     * 本地测试初始化spark session 的工具类.
     * @param appName
     * @return
     */
    public static SparkSession getLocalTestSession(String appName) {
        SparkSession ss = SparkSession.builder().appName(appName).master("local[*]")
                //       .enableHiveSupport()
                .config( "spark.serializer", "org.apache.spark.serializer.KryoSerializer" )
                .getOrCreate();
//        ss.conf().set( "spark.sql.broadcastTimeout", 4 * 60 * 60 + "" );
//        ss.conf().set( "hive.exec.dynamic.partition.mode", "nonstrict" );
//        //使用Kryo序列化库
//        ss.conf().set( "spark.kryoserializer.buffer.max", "102400" );
//        ss.conf().set( "spark.kryoserializer.buffer", "1024" );
//        ss.conf().set( "spark.shuffle.file.buffer", "64" );
//        ss.conf().set( "spark.sql.crossJoin.enabled", "true" );
        return ss;
    }
    public static SparkSession initSparkSession(String appName) {
        return SparkSessionUtil.getDefaultSession( appName );
    }
}
