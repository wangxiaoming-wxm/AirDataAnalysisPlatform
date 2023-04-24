package com.air.data.ml;
import com.air.data.common.utils.SparkSessionUtil;
import com.air.data.scalaUtils.DUtils;
import com.air.data.scalaUtils.SparkMLlibUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于SparkMLlib机器学习算法预测航班是否延误，选择随机森林、多元线性回归、神经网络、朴素贝叶斯等算法进行预测
 */
public class FlightDelaysForest {

    public static final String HIVE_WRITE_TABLE = "beta.dws_chain_is_full_base_ed"; //beta.tmp_dws_chain_is_full_ed, beta.temp_dws_chain_is_full_ed
    public static final String HIVE_WRITE_TABLE_NEW = "beta.temp_dws_chain_is_full_new_ed";
    public static final String HIVE_WRITE_TABLE_ACC_INFO =  "beta.temp_acc_before_info"; //beta.acc_before_info
    public static final String HIVE_WRITE_TABLE_ACC_HOUR_INFO =  "beta.temp_acc_hour_info"; //beta.acc_hour_info
    public static final String HIVE_WRITE_TABLE_XGB_MODEL_INFO=  "beta.temp_dws_xgb_chain_is_full_ed";//beta.dws_xgb_chain_is_full_ed
    public static final String HIVE_WRITE_TABLE_RM_MODEL_INFO=  "beta.dws_rf_chain_is_full_ed";
    public static final String HIVE_WRITE_TABLE_MLPC_MODEL_INFO=  "beta.dws_mlpc_chain_is_full_ed";
    public static final String HIVE_WRITE_TABLE_BAYES_MODEL_INFO=  "beta.dws_bayes_chain_is_full_ed";
    public static final String HIVE_WRITE_TABLE_MODEL_RESULT = "beta.dws_chain_is_full_model_result_model_result_ed";
    public static final String HIVE_WRITE_TABLE_MODEL = "beta.dws_chain_is_full_forecast_ed";
    private static final Logger LOGGER = LoggerFactory.getLogger( FlightDelaysForest.class );

    public static void main(String[] args) {
        SparkSession sparkSession = SparkSessionUtil.initSparkSession( "DwsChainIfFullForecastMain" );
        String dt = DUtils.getDateChange(DUtils.getCurrentDate(), 0, 0, -1);

        String bayesTrainingDataPath        =    "/tmp/model/spark/bayesTraining/";
        String bayesPredictionDataPath      =    "/tmp/model/spark/bayesPrediction/";
        String xgboostTrainingPath          =    "/tmp/model/spark/xgboostTraining/";
        String xgboostPredictionDataPath    =    "/tmp/model/spark/xgboostPrediction/";
        String libSVMTrainingPath           =    "/tmp/model/spark/libSVMTraining/";
        String libSVMPredictionPath         =    "/tmp/model/spark/libSVMPrediction/";

        try {
             run(sparkSession, dt, bayesTrainingDataPath, bayesPredictionDataPath, xgboostTrainingPath, xgboostPredictionDataPath , libSVMTrainingPath, libSVMPredictionPath) ;
        } catch (Exception e) {
            LOGGER.error( "DwsChainIsFullForecastTask abnormal", e );
        } finally {
            sparkSession.stop();
        }

    }
    public static void run(SparkSession sparkSession, String dt, String bayesTrainingDataPath, String bayesPredictionDataPath, String xgboostTrainingPath, String xgboostPredictionDataPath, String libSVMTrainingPath, String libSVMPredictionPath) {
        generateTrainingData(sparkSession,dt,bayesTrainingDataPath,bayesPredictionDataPath,xgboostTrainingPath,xgboostPredictionDataPath ,libSVMTrainingPath,libSVMPredictionPath) ;
        chainRoomIsFullForecast(sparkSession,dt,bayesTrainingDataPath,bayesPredictionDataPath,xgboostTrainingPath,xgboostPredictionDataPath ,libSVMTrainingPath,libSVMPredictionPath);
    }

    public static void chainRoomIsFullForecast(SparkSession sparkSession, String dt, String bayesTrainingDataPath, String bayesPredictionDataPath, String xgboostTrainingPath, String xgboostPredictionDataPath, String libSVMTrainingPath, String libSVMPredictionPath) {

        //保存当天待预测酒店数据
        String waitForPredictionFileSql = "select concat_ws(',',cast(flag_id as string),cast(if_full as string),chain_name,acc_date ) as waitForPrediction  from (\n" +
                "select row_number() over (partition by if_full*0   order by chain_name) flag_id,if_full, chain_name,acc_date from (select * from "
                + HIVE_WRITE_TABLE + " where acc_date = '" + dt + "' and chain_name is not null order by chain_name) tmp ) t ";
        sparkSession.sql(waitForPredictionFileSql).repartition(1).createOrReplaceTempView("waitForPrediction");

        //预测 t1 left join waitForPrediction t2 on t1.flag_id = t2.flag_id
        Dataset<Row> sparkXgboost = SparkMLlibUtils.sparkXgboost(sparkSession,xgboostTrainingPath,xgboostPredictionDataPath);
        sparkXgboost.repartition(1).write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_XGB_MODEL_INFO);
        sparkXgboost.createOrReplaceTempView("sparkXgboost");
        sparkSession.sql("select row_number() over (order by classIndex*0) as flag_id,cast(classIndex as int) label,cast(prediction as int) as prediction  from sparkXgboost ").repartition(1).createOrReplaceTempView("xgb");
        sparkSession.sql("select t1.flag_id,t1.label,t1.prediction,t2.waitForPrediction from xgb t1 left join waitForPrediction t2 on t1.flag_id = split(t2.waitForPrediction,',')[0]")
                .repartition(1).write().mode("overwrite").saveAsTable("beta.dws_chain_is_full_model_result_model_result_new_ed_"+dt.replace("-",""));;

        String resultSql = "select flag_id,label,prediction,\n" +
                " split(waitForPrediction,',')[0]  flag_index, \n" +
                " split(waitForPrediction,',')[1]  label_index, \n" +
                " split(waitForPrediction,',')[2]  chain_name, \n" +
                " split(waitForPrediction,',')[3]  acc_date \n" +
                " from beta.dws_chain_is_full_model_result_model_result_new_ed_"+ dt.replace("-","") +" order by chain_name";
        sparkSession.sql(resultSql).repartition(1).write().mode("overwrite").saveAsTable("beta.dws_chain_is_full_forecast_new_ed_"+dt.replace("-",""));




        Dataset<Row> sparkRandomForest = SparkMLlibUtils.sparkRandomForest(sparkSession,libSVMTrainingPath,libSVMPredictionPath);
        sparkRandomForest.write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_RM_MODEL_INFO);
        sparkRandomForest.select("indexedLabel").write().mode("overwrite").format("text").save("/tmp/model/spark/sparkRandomForest/indexedLabel/");
        sparkRandomForest.select("prediction").write().mode("overwrite").format("text").save("/tmp/model/spark/sparkRandomForest/prediction/");




        Dataset<Row> sparkMLPC = SparkMLlibUtils.sparkMLPC(sparkSession,libSVMTrainingPath,libSVMPredictionPath).repartition(1);
        sparkMLPC.write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_MLPC_MODEL_INFO);
        sparkMLPC.select("label").write().mode("overwrite").format("text").save("/tmp/model/spark/sparkMLPC/label/");
        sparkMLPC.select("prediction").write().mode("overwrite").format("text").save("/tmp/model/spark/sparkMLPC/prediction/");


        Dataset<Row> sparkNaiveBayes = SparkMLlibUtils.sparkNaiveBayes(sparkSession,bayesTrainingDataPath,bayesPredictionDataPath).repartition(1);
        sparkNaiveBayes.write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_BAYES_MODEL_INFO);
        sparkNaiveBayes.select("label").write().mode("overwrite").format("text").save("/tmp/model/spark/sparkNaiveBayes/label/");
        sparkNaiveBayes.select("prediction").write().mode("overwrite").format("text").save("/tmp/model/spark/sparkNaiveBayes/prediction/");

    }
    public static void generateTrainingData(SparkSession sparkSession, String dt, String bayesTrainingDataPath, String bayesPredictionDataPath, String xgboostTrainingPath, String xgboostPredictionDataPath, String libSVMTrainingPath, String libSVMPredictionPath) {
        try {



            //新口径满房判断
            String newIsFullSql = "select chain_name,show_date,case\n" +
                    "when real_ratio >=0.95 then  1 else 0 end is_full,\n" +
                    "real_ratio as day0_tw_ratio_fore from \n" +
                    "(select \n" +
                    "chain_name,business_date show_date,sum(total_night_count)/sum(chain_room_num) real_ratio\n" +
                    "from  \n" +
                    "(\n" +
                    "select \n" +
                    "chain_id, \n" +
                    "chain_name, \n" +
                    "business_date, \n" +
                    "sum(total_night_count) total_night_count, \n" +
                    "max(chain_room_num) chain_room_num       \n" +
                    "from gdl.gdl_new_business_report where chain_id != 888888 and dt = '"+dt+"'\n" +
                    "group by chain_id,chain_name,business_date\n" +
                    ") a \n" +
                    "group by chain_name,business_date)\n" +
                    " t\n" +
                    " ";
            sparkSession.sql(newIsFullSql).repartition(1).write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_NEW);


            //---今日计算今日流量--
            String accHourInfoSql = "select chain_name,acc_date,compute_hour,\n" +
                    " (sum(total_inventory)-sum(remain_inventory))/sum(total_inventory) ratio_fore\n" +
                    " from realtime.realtime_stores_traffic_results_new \n" +
                    " where compute_date=to_date(date_sub(acc_date,0)) and compute_hour in (9,23)" +
                    //" AND acc_date >= '2021-06-23' and  dt >= '2021-06-23'\n" +
                    " group by chain_name,acc_date,compute_hour";
            sparkSession.sql(accHourInfoSql).repartition(10).write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_ACC_HOUR_INFO);


            //---昨日计算今日23点流量--
            String accBeforeInfoSql = "select chain_name,acc_date,compute_hour,\n" +
                    " (sum(total_inventory)-sum(remain_inventory))/sum(total_inventory) AS tw_ratio_fore_before\n" +
                    " from realtime.realtime_stores_traffic_results_new \n" +
                    " where compute_date=to_date(date_sub(acc_date,1)) and compute_hour=23" +
                    //" AND acc_date >= '2021-06-23' and  dt >= '2021-06-23'\n" +
                    " group by chain_name,acc_date,compute_hour";
            sparkSession.sql(accBeforeInfoSql).repartition(10).write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE_ACC_INFO);


            String accinfoSql = "select chain_name,acc_date,\n" +
                    " sum(case when compute_hour=9 then ratio_fore else 0 end)  ni_ratio_fore,\n" +
                    " sum(case when compute_hour=23 then ratio_fore else 0 end) tw_ratio_fore\n" +
                    " from " + HIVE_WRITE_TABLE_ACC_HOUR_INFO + "\n" +
                    " group by chain_name,acc_date";
            sparkSession.sql(accinfoSql).createOrReplaceTempView("acc_info");


            //---取之前14天的9点及23点流量----
            String end1InfoSql = "select a.chain_name,a.acc_date acc_date_a,b.acc_date acc_date_b,\n" +
                    "a.tw_ratio_fore tw_ratio_fore_a,\n" +
                    "b.tw_ratio_fore,\n" +
                    "a.ni_ratio_fore ni_ratio_fore_a,\n" +
                    "b.ni_ratio_fore\n" +
                    "from acc_info a left join acc_info b\n" +
                    "on a.acc_date>=b.acc_date and a.acc_date<=to_date(date_sub(b.acc_date,-14)) \n" +
                    "and a.chain_name=b.chain_name";
            sparkSession.sql(end1InfoSql).createOrReplaceTempView("end1_info");

            String end2InfoSql = "select a.chain_name,\n" +
                    "acc_date_a acc_date,\n" +
                    "sum(case when acc_date_a=acc_date_b then tw_ratio_fore_a else 0 end) day0_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-1)) then tw_ratio_fore else 0 end) day1_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-2)) then tw_ratio_fore else 0 end) day2_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-3)) then tw_ratio_fore else 0 end) day3_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-4)) then tw_ratio_fore else 0 end) day4_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-5)) then tw_ratio_fore else 0 end) day5_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-6)) then tw_ratio_fore else 0 end) day6_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-7)) then tw_ratio_fore else 0 end) day7_tw_ratio_fore,\n" +
                    "sum(case when acc_date_a=acc_date_b then ni_ratio_fore_a else 0 end) day0_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-1)) then ni_ratio_fore else 0 end) day1_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-2)) then ni_ratio_fore else 0 end) day2_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-3)) then ni_ratio_fore else 0 end) day3_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-4)) then ni_ratio_fore else 0 end) day4_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-5)) then ni_ratio_fore else 0 end) day5_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-6)) then ni_ratio_fore else 0 end) day6_ni_ratio_fore,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-7)) then ni_ratio_fore else 0 end) day7_ni_ratio_fore \n" +
                    "from end1_info a \n" +
                    "group by acc_date_a,a.chain_name";
            sparkSession.sql(end2InfoSql).createOrReplaceTempView("end2_info");

            String base1InfoSql = "select a.chain_name,a.acc_date acc_date_a,b.acc_date acc_date_b,\n" +
                    "a.tw_ratio_fore_before AS tw_ratio_fore_before_a,\n" +
                    "b.tw_ratio_fore_before\n" +
                    "from  " + HIVE_WRITE_TABLE_ACC_INFO + "  a left join " + HIVE_WRITE_TABLE_ACC_INFO + "  b\n" +
                    "on a.acc_date>=b.acc_date and a.acc_date<=to_date(date_sub(b.acc_date,-7)) \n" +
                    "and a.chain_name=b.chain_name";
            sparkSession.sql(base1InfoSql).createOrReplaceTempView("base1_info");


            String base2InfoSql = "select chain_name,\n" +
                    "acc_date_a acc_date,\n" +
                    "sum(case when acc_date_a=acc_date_b then tw_ratio_fore_before_a else 0 end) day0_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-1)) then tw_ratio_fore_before else 0 end) day1_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-2)) then tw_ratio_fore_before else 0 end) day2_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-3)) then tw_ratio_fore_before else 0 end) day3_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-4)) then tw_ratio_fore_before else 0 end) day4_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-5)) then tw_ratio_fore_before else 0 end) day5_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-6)) then tw_ratio_fore_before else 0 end) day6_tw_ratio_fore_before,\n" +
                    "sum(case when acc_date_a=to_date(date_sub(acc_date_b,-7)) then tw_ratio_fore_before else 0 end) day7_tw_ratio_fore_before\n" +
                    "from base1_info \n" +
                    "group by chain_name,acc_date_a";
            sparkSession.sql(base2InfoSql).createOrReplaceTempView("base2_info");


            //最终训练数据源
            String finalResultSql =
                    " select  \n" +
                            " chain_name,\n" +
                            " is_full if_full,\n" +
                            " acc_date,\n" +
                            " day0_tw_ratio_fore_new as day0_tw_ratio_fore,\n" +
                            " day1_tw_ratio_fore,\n" +
                            " day2_tw_ratio_fore,\n" +
                            " day3_tw_ratio_fore,\n" +
                            " day4_tw_ratio_fore,\n" +
                            " day5_tw_ratio_fore,\n" +
                            " day6_tw_ratio_fore,\n" +
                            " day7_tw_ratio_fore,\n" +
                            " day0_ni_ratio_fore,\n" +
                            " day1_ni_ratio_fore,\n" +
                            " day2_ni_ratio_fore,\n" +
                            " day3_ni_ratio_fore,\n" +
                            " day4_ni_ratio_fore,\n" +
                            " day5_ni_ratio_fore,\n" +
                            " day6_ni_ratio_fore,\n" +
                            " day7_ni_ratio_fore,\n" +
                            " day0_tw_ratio_fore_before,\n" +
                            " day1_tw_ratio_fore_before,\n" +
                            " day2_tw_ratio_fore_before,\n" +
                            " day3_tw_ratio_fore_before,\n" +
                            " day4_tw_ratio_fore_before,\n" +
                            " day5_tw_ratio_fore_before,\n" +
                            " day6_tw_ratio_fore_before,\n" +
                            " day7_tw_ratio_fore_before\n" +
                            "  from ("+
                            " select t1.*,t2.day0_tw_ratio_fore as day0_tw_ratio_fore_new,t2.is_full from (\n" +
                            " select a.*, \n" +
                            " day0_tw_ratio_fore_before,day1_tw_ratio_fore_before,day2_tw_ratio_fore_before, \n" +
                            " day3_tw_ratio_fore_before,\n" +
                            " day4_tw_ratio_fore_before,\n" +
                            " day5_tw_ratio_fore_before,\n" +
                            " day6_tw_ratio_fore_before,\n" +
                            " day7_tw_ratio_fore_before\n" +
                            " from end2_info a left join base2_info b  \n" +
                            " on a.acc_date=b.acc_date and a.chain_name=b.chain_name  \n" +
                            " ) t1 left join " + HIVE_WRITE_TABLE_NEW + " t2 on t1.chain_name = t2.chain_name and t1.acc_date = t2.show_date) t\n" +
                            " where is_full is not null  and  \n" +
                            " day0_tw_ratio_fore_new > 0 and day0_tw_ratio_fore_new is not null\n" ;
            sparkSession.sql(finalResultSql).repartition(1).write().mode("overwrite").saveAsTable(HIVE_WRITE_TABLE);


            //生成xgboost所需要csv训练数据
            String xgboostTrainingDataSql = "select concat(cast(if_full as string), \n " +
                    " concat(',' ,cast(day1_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day2_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day3_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day4_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day5_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day6_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day7_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day0_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day1_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day2_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day3_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day4_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day5_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day6_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day7_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day0_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day1_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day2_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day3_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day4_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day5_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day6_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day7_tw_ratio_fore_before as string ))\n" +
                    " ) trainingdata " +
                    " from " + HIVE_WRITE_TABLE + " where acc_date < '" + dt + "' ";
            sparkSession.sql(xgboostTrainingDataSql).repartition(1).write().mode("overwrite").format("text").save(xgboostTrainingPath);

            //生成xgboost所需要csv预测数据
            String xgboostPredictionDataSql = "select concat(cast(if_full as string), \n " +
                    " concat(',' ,cast(day1_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day2_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day3_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day4_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day5_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day6_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day7_tw_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day0_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day1_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day2_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day3_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day4_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day5_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day6_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day7_ni_ratio_fore as string)), \n" +
                    " concat(',' ,cast(day0_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day1_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day2_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day3_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day4_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day5_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day6_tw_ratio_fore_before as string )),\n" +
                    " concat(',' ,cast(day7_tw_ratio_fore_before as string ))\n" +
                    " ) xgb " +
                    " from (select * from " + HIVE_WRITE_TABLE + " where acc_date = '" + dt + "' and chain_name is not null   order by chain_name ) tmp order by chain_name  ";
            sparkSession.sql(xgboostPredictionDataSql).repartition(1).write().mode("overwrite").format("text").save(xgboostPredictionDataPath);


            //朴素贝叶斯训练数据源
            String bayesTrainingDataSql = "select concat(chain_name, \n" +
                    " concat('~' ,cast(acc_date as string)), \n" +
                    " concat('`' ,cast(if_full as string)),  \n" +
                    " concat(',' ,cast(day1_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day2_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day3_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day4_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day5_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day6_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day7_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day0_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day1_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day2_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day3_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day4_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day5_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day6_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day7_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day0_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day1_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day2_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day3_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day4_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day5_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day6_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day7_tw_ratio_fore_before as string ))\n" +
                    " ) trainingdata " +
                    " from " + HIVE_WRITE_TABLE + " where acc_date < '" + dt + "' ";
            //sparkSession.sql(bayesTrainingDataSql).repartition(1).write().mode("overwrite").format("text").save(bayesTrainingDataPath);

            //朴素贝叶斯预测数据源
            String bayesPredictionDataSql = "select concat(chain_name, \n" +
                    " concat('~' ,cast(acc_date as string)), \n" +
                    " concat('`' ,cast(if_full as string)),  \n" +
                    " concat(',' ,cast(day1_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day2_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day3_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day4_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day5_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day6_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day7_tw_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day0_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day1_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day2_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day3_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day4_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day5_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day6_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day7_ni_ratio_fore as string)), \n" +
                    " concat('|' ,cast(day0_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day1_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day2_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day3_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day4_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day5_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day6_tw_ratio_fore_before as string )),\n" +
                    " concat('|' ,cast(day7_tw_ratio_fore_before as string ))\n" +
                    " ) bayes " +
                    " from (select * from " + HIVE_WRITE_TABLE + " where acc_date = '" + dt + "' and chain_name is not null order by chain_name ) tmp order by chain_name  ";
            //sparkSession.sql(bayesPredictionDataSql).write().mode("overwrite").format("text").save(bayesPredictionDataPath);





            //生成随机森林和神经网络的LibSVM训练数据
            String libSVMTrainingDataSql = "select concat_ws(' ',cast(if_full as string),\n" +
                    "concat('1:' ,day1_tw_ratio_fore),\n" +
                    "concat('2:' ,day2_tw_ratio_fore),\n" +
                    "concat('3:' ,day3_tw_ratio_fore),\n" +
                    "concat('4:' ,day4_tw_ratio_fore),\n" +
                    "concat('5:' ,day5_tw_ratio_fore),\n" +
                    "concat('6:' ,day6_tw_ratio_fore),\n" +
                    "concat('7:' ,day7_tw_ratio_fore),\n" +
                    "concat('8:' ,day0_ni_ratio_fore),\n" +
                    "concat('9:' ,day1_ni_ratio_fore),\n" +
                    "concat('10:',day2_ni_ratio_fore),\n" +
                    "concat('11:',day3_ni_ratio_fore),\n" +
                    "concat('12:',day4_ni_ratio_fore),\n" +
                    "concat('13:',day5_ni_ratio_fore),\n" +
                    "concat('14:',day6_ni_ratio_fore),\n" +
                    "concat('15:',day7_ni_ratio_fore),\n" +
                    "concat('16:',day0_tw_ratio_fore_before),\n" +
                    "concat('17:',day1_tw_ratio_fore_before),\n" +
                    "concat('18:',day2_tw_ratio_fore_before),\n" +
                    "concat('19:',day3_tw_ratio_fore_before),\n" +
                    "concat('20:',day4_tw_ratio_fore_before),\n" +
                    "concat('21:',day5_tw_ratio_fore_before),\n" +
                    "concat('22:',day6_tw_ratio_fore_before),\n" +
                    "concat('23:',day7_tw_ratio_fore_before)\n" +
                    ") libSVMData " +
                    " from " + HIVE_WRITE_TABLE + " where acc_date < '" + dt + "' ";
            //sparkSession.sql(libSVMTrainingDataSql).repartition(1).write().mode("overwrite").format("text").save(libSVMTrainingPath);

            //生成随机森林和神经网络的LibSVM预测数据
            String libSVMPredictionDataSql = "select concat_ws(' ',cast(if_full as string),\n" +
                    "concat('1:' ,cast(day1_tw_ratio_fore        as string)), \n" +
                    "concat('2:' ,cast(day2_tw_ratio_fore        as string)), \n" +
                    "concat('3:' ,cast(day3_tw_ratio_fore        as string)), \n" +
                    "concat('4:' ,cast(day4_tw_ratio_fore        as string)), \n" +
                    "concat('5:' ,cast(day5_tw_ratio_fore        as string)), \n" +
                    "concat('6:' ,cast(day6_tw_ratio_fore        as string)), \n" +
                    "concat('7:' ,cast(day7_tw_ratio_fore        as string)), \n" +
                    "concat('8:' ,cast(day0_ni_ratio_fore        as string)), \n" +
                    "concat('9:' ,cast(day1_ni_ratio_fore        as string)), \n" +
                    "concat('10:',cast(day2_ni_ratio_fore        as string)), \n" +
                    "concat('11:',cast(day3_ni_ratio_fore        as string)), \n" +
                    "concat('12:',cast(day4_ni_ratio_fore        as string)), \n" +
                    "concat('13:',cast(day5_ni_ratio_fore        as string)), \n" +
                    "concat('14:',cast(day6_ni_ratio_fore        as string)), \n" +
                    "concat('15:',cast(day7_ni_ratio_fore        as string)), \n" +
                    "concat('16:',cast(day0_tw_ratio_fore_before as string)), \n" +
                    "concat('17:',cast(day1_tw_ratio_fore_before as string)), \n" +
                    "concat('18:',cast(day2_tw_ratio_fore_before as string)), \n" +
                    "concat('19:',cast(day3_tw_ratio_fore_before as string)), \n" +
                    "concat('20:',cast(day4_tw_ratio_fore_before as string)), \n" +
                    "concat('21:',cast(day5_tw_ratio_fore_before as string)), \n" +
                    "concat('22:',cast(day6_tw_ratio_fore_before as string)), \n" +
                    "concat('23:',cast(day7_tw_ratio_fore_before as string))  \n" +
                    ") libSVM " +
                    " from (select * from " + HIVE_WRITE_TABLE + " where acc_date = '" + dt + "' and chain_name is not null   order by chain_name ) tmp   order by chain_name   ";
            //sparkSession.sql(libSVMPredictionDataSql).repartition(1).write().mode("overwrite").format("text").save(libSVMPredictionPath);


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
