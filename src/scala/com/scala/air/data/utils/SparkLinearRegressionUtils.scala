package com.scala.air.data.utils

import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.{DataFrame, SparkSession}


object SparkLinearRegressionUtils {
  def sparkLR(sparkSession:SparkSession,trainingFilePath:String,waitForPredictionsFilePath:String): DataFrame = {
    import sparkSession.implicits._

    // 读取样本数据
    val trainingDataset = sparkSession.read.textFile(trainingFilePath)

    // 读取待预测数据
    val waitForPredictionsDataset = sparkSession.read.textFile(waitForPredictionsFilePath)

    //处理样本数据
    val trainingParseData = trainingDataset.map { line =>
      val str =line.trim.split("~")
      val features = str(3).trim.split("#").map(_.toDouble)
      // label为因变量，即待预测值，后面的features向量为自变量，即特征数据
      LabeledPoint(str(2).trim.toDouble, Vectors.dense(features))
    }

    //处理待预测数据
    val waitForPredictionsParseData = waitForPredictionsDataset.map { line =>
      val str =line.trim.split("~")
      val features = str(3).trim.split("#").map(_.toDouble)
      LabeledPoint(str(2).trim.toDouble, Vectors.dense(features))
    }

    // 将样本数据按照8:2抽取出来，构建训练集和测试集
    val arr = trainingParseData.randomSplit(Array(0.8, 0.2), 3)

    // 构建模型
    val linearRegression = new LinearRegression()
      .setLabelCol("label")
      .setFeaturesCol("features")
      .setTol(0.000001) // 收敛值，越小越精准
      .setMaxIter(8000) // 迭代次数
      .setFitIntercept(true) // 是否有w0截距

    // 使用划分完的训练集训练模型
    val model = linearRegression.fit(arr(0))
    println("训练结果")
    println("权重： " + model.coefficients)
    println("截距：" + model.intercept)
    println("特征数：" + model.numFeatures)


    //使用划分完的测试集测试模型
    val TestSummary = model.evaluate(arr(1))
    println("测试结果")
    println("均方差：" + TestSummary.meanSquaredError)
    println("平均绝对值误差：" + TestSummary.meanAbsoluteError)
    println("测试数据的条目数：" + TestSummary.numInstances)
    println("模型拟合度：" + TestSummary.r2)

    //将待预测数据输入模型进行预测
    val waitForPredictionsSummary = model.evaluate(waitForPredictionsParseData)
    val waitForPredictionsResults = waitForPredictionsSummary.predictions

    //返回模型预测结果
    return  waitForPredictionsResults
  }

}
