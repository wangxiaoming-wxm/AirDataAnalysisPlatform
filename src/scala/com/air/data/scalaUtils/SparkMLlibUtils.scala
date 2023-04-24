package com.air.data.scalaUtils

import ml.dmlc.xgboost4j.scala.spark.{XGBoostClassificationModel, XGBoostClassifier}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{MultilayerPerceptronClassifier, NaiveBayes, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}



object SparkMLlibUtils {

  /*
   * 多元线性回归
   */
  def sparkLinearRegression(sparkSession:SparkSession,trainingFilePath:String,waitForPredictionsFilePath:String): DataFrame = {
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

  /*
   *  随机森林
   */
  def sparkRandomForest(sparkSession:SparkSession,libSVMTrainingPath:String,libSVMPredictionPath:String): DataFrame = {
    //加载训练数据源
    val libSVMTrainingData = sparkSession.read.format("libsvm").load(libSVMTrainingPath)
    //加载预测数据源
    val libSVMPredictionData = sparkSession.read.format("libsvm").load(libSVMPredictionPath)
    println("随机森林预测数据源")
    libSVMPredictionData.show(10)

    //标识整个数据集的标识列和索引列
    val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(libSVMTrainingData)
    val featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(4).fit(libSVMTrainingData)

    //拆分数据为训练集和测试集（8:2）
    val Array(trainingData, testData) = libSVMTrainingData.randomSplit(Array(0.9, 0.1))
    //创建模型
    val randomForest = new RandomForestClassifier().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setNumTrees(200).setFeatureSubsetStrategy("auto").setImpurity("gini").setMaxDepth(5).setMaxBins(32)

    //转化初始数据
    val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)

    //使用管道运行转换器和随机森林算法
    val pipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, randomForest, labelConverter))

    //训练模型
    val model = pipeline.fit(trainingData)

    //预测
    val result = model.transform(libSVMPredictionData)

    //创建评估函数，计算正确率
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction").setMetricName("accuracy")

    //输出正确率指标
    val accuracy = evaluator.evaluate(result)
    println("随机森林整体正确率 : " + accuracy)

    //输出预测结果
    return result
  }

  /*
   *  xgboost
   */
  def sparkXgboost(sparkSession:SparkSession, xgboostTrainingPath:String,xgboostPredictionDataPath:String): DataFrame = {

    //加载数据结构
    val schema = new StructType(Array(
      StructField("class", DoubleType, nullable = false),
      StructField("day1_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day2_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day3_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day4_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day5_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day6_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day7_tw_ratio_fore", DoubleType, nullable = true),
      StructField("day0_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day1_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day2_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day3_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day4_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day5_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day6_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day7_ni_ratio_fore", DoubleType, nullable = true),
      StructField("day0_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day1_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day2_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day3_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day4_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day5_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day6_tw_ratio_fore_before", DoubleType, nullable = true),
      StructField("day7_tw_ratio_fore_before", DoubleType, nullable = true)
    ))

    //加载训练、测试数据源
    val xgboostTrainingData = sparkSession.read.schema(schema).csv(xgboostTrainingPath)
    val xgboostPredictionData = sparkSession.read.schema(schema).csv(xgboostPredictionDataPath)

    // 把字符串class转换成数字数字class
    val stringIndexerTrain = new StringIndexer().setInputCol("class").setOutputCol("classIndex").fit(xgboostTrainingData)
    val stringIndexerPrediction = new StringIndexer().setInputCol("class").setOutputCol("classIndex").fit(xgboostPredictionData)

    //把原有的字符串class删除掉
    val labelTransformedTrain = stringIndexerTrain.transform(xgboostTrainingData).drop("class")
    val labelTransformedPrediction = stringIndexerPrediction.transform(xgboostPredictionData).drop("class")

    // 将多个字段合并成在一起,组成features
    val vectorAssembler = new VectorAssembler()
      .setInputCols(Array(
        "day1_tw_ratio_fore",
        "day2_tw_ratio_fore",
        "day3_tw_ratio_fore",
        "day4_tw_ratio_fore",
        "day5_tw_ratio_fore",
        "day6_tw_ratio_fore",
        "day7_tw_ratio_fore",
        "day0_ni_ratio_fore",
        "day1_ni_ratio_fore",
        "day2_ni_ratio_fore",
        "day3_ni_ratio_fore",
        "day4_ni_ratio_fore",
        "day5_ni_ratio_fore",
        "day6_ni_ratio_fore",
        "day7_ni_ratio_fore",
        "day0_tw_ratio_fore_before",
        "day1_tw_ratio_fore_before",
        "day2_tw_ratio_fore_before",
        "day3_tw_ratio_fore_before",
        "day4_tw_ratio_fore_before",
        "day5_tw_ratio_fore_before",
        "day6_tw_ratio_fore_before",
        "day7_tw_ratio_fore_before"
      )).setOutputCol("features")

    //将数据集切分成训集和测试集
    val xgbTrain = vectorAssembler.transform(labelTransformedTrain).select("features", "classIndex")
    val xgbPrediction = vectorAssembler.transform(labelTransformedPrediction).select("features", "classIndex")

    val splitXgbInput = xgbTrain.randomSplit(Array(0.9, 0.1))
    val trainXgbInput = splitXgbInput(0)

    // 0.2 200
    val xgbParams = Map("eta" -> 0.2f,"objective" -> "multi:softmax","num_workers" -> 1,"max_depth" -> 5,"num_class" -> 2,"num_round" -> 300)

    // 创建xgboost函数,指定特征向量和标签
    val xgbClassifierTrain = new XGBoostClassifier(xgbParams).setFeaturesCol("features").setLabelCol("classIndex")

    // 开始训练
    val xgbClassificationModel: XGBoostClassificationModel = xgbClassifierTrain.fit(trainXgbInput)

    // 预测
    val result = xgbClassificationModel.transform(xgbPrediction)

    //创建评估函数，计算正确率
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("classIndex").setPredictionCol("prediction").setMetricName("accuracy")

    val accuracy = evaluator.evaluate(result)
    println("Xgboost整体正确率 : " + accuracy)

    //保存预测结果
    return result
  }


  /*
   * 基于神经网络的MultilayerPerceptronClassifier
   */
  def sparkMLPC(sparkSession: SparkSession, libSVMTrainingPath: String,libSVMPredictionPath:String): DataFrame = {

    // 加载训练、预测数据源
    val libSVMTrainingData = sparkSession.read.format("libsvm").load(libSVMTrainingPath)
    val libSVMPredictionData = sparkSession.read.format("libsvm").load(libSVMPredictionPath)
    println("神经网络预测数据源")
    libSVMPredictionData.head(10)


    // 拆分成训练集和测试集
    val splits = libSVMTrainingData.randomSplit(Array(0.9, 0.1), seed = 123L)
    val train = splits(0)

    // 指定神经网络的图层： 输入层23个结点(即23个特征)；5个隐藏层，输出层2个结点(即分为2类)
    val layers = Array[Int](23, 15, 13, 12, 11, 10, 2) //1000,90.9

    // 建立MLPC训练器并设置参数
    val trainer = new MultilayerPerceptronClassifier().setLayers(layers).setBlockSize(128).setSeed(123L).setMaxIter(200) //1200

    // 训练模型
    val model = trainer.fit(train)

    // 用训练好的模型预测测试集的结果
    val result = model.transform(libSVMPredictionData)
    val predictionAndLabels = result.select("prediction", "label")

    // 计算误差并输出
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictionAndLabels)

    println("基于神经网络的MLPC整体正确率 : " + accuracy )

    // 输出结果
    return result
  }

  /*
   *  朴素贝叶斯
   */

  def sparkNaiveBayes(sparkSession: SparkSession, bayesTrainingDataPath:String,bayesPredictionDataPath:String): DataFrame = {
    import sparkSession.implicits._

    // 加载训练、预测数据源
    val bayesTrainingData = sparkSession.read.textFile(bayesTrainingDataPath)
    val bayesPredictionData = sparkSession.read.textFile(bayesPredictionDataPath)

    //处理训练数据
    val trainingData = bayesTrainingData.map { line =>
      val str =line.trim.split(",")
      val features = str(1).trim.split("\\|").map(_.toDouble)
      // label为因变量，即待预测值，后面的features向量为自变量，即特征数据
      LabeledPoint(str(0).split("`")(1).toDouble, Vectors.dense(features))
    }

    //处理预测数据
    val predictionData = bayesPredictionData.map { line =>
      val str =line.trim.split(",")
      val features = str(1).trim.split("\\|").map(_.toDouble)
      // label为因变量，即待预测值，后面的features向量为自变量，即特征数据
      LabeledPoint(str(0).split("`")(1).toDouble, Vectors.dense(features))
    }
    println("朴素贝叶斯预测数据源")
    predictionData.head(10)


    //将样本数据分为训练样本和测试样本
    val trainingDataSet = trainingData.randomSplit(Array(0.6,0.4),seed = 23L)//对数据进行分配
    val train=trainingDataSet(0)//训练数据

    //建立贝叶斯分类模型，并进行训练
    val naiveBayes = new NaiveBayes
    val model=naiveBayes.fit(train)

    // 用训练好的模型预测
    val result = model.transform(predictionData)
    val predictionAndLabels = result.select("prediction", "label")

    // 计算误差并输出
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction").setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictionAndLabels)

    println("朴素贝叶斯整体正确率 : " + accuracy )

    // 输出结果
    return result
  }
}

