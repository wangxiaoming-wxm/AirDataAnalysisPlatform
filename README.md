# 航空数据采集分析平台

#### 平台介绍
AirDataAnalysisPlatform项目是基于SpringBoot+MyBatis Plus构建的航空数据采集平台，主要通过代码控制数据采集周期、数据采集起止点，通过Jsoup框架获取真实数据，之后写入MySQL，即完成数据采集链路。

#### 软件架构
- springBoot 2.6.7
- mybatis plus 3.5.0
- jsoup 1.14.3
- mysql 8.0.27
- beanSearch 3.8.1
- tess4j 5.3.0

#### 安装教程

1.  修改application-dev.yml中mysql数据的连接URL和用户名密码
2.  mvn clean install编译代码
3.  进入sql目录，在自己的mysql执行air_data_source.sql语句，创建相关的表
4.  需要安装tesseract-ocr，下载地址：https://digi.bib.uni-mannheim.de/tesseract/ 安装完成后，设置环境变量：TESSDATA_PREFIX，指向安装目录下的tessdata文件夹。
5.  进入com.air.data包，打开Application类，点击运行即可

#### 使用说明

1.  本平台作为数据采集代理平台，运行在本地，且需要公网连接。
2.  本平台数据采集接口方法为GET，访问地址为：
```shell
http://127.0.0.1:8081/airDataSource/add
```
3.  本平台数据采集接口参数为dayId：
```shell
http://127.0.0.1:8081/airDataSource/add?dayId=2023-02-27
```
4.  本平台图片OCR识别并入库的接口参数为dayId：
```shell
http://localhost:8081/images/parser?dayId=2023-02-27
```

#### 参与贡献

1.  Fork 本仓库
2.  新建本人分支
3.  提交代码
4.  新建 Pull Request