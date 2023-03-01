/*
 Navicat MySQL Data Transfer

 Source Server         : 192.168.56.101
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.56.101:3306
 Source Schema         : air_data

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 01/03/2023 09:53:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dim_airport
-- ----------------------------
DROP TABLE IF EXISTS `dim_airport`;
CREATE TABLE `dim_airport`  (
  `province_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机场所在省份',
  `city_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机场所在城市',
  `airport_code_3` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机场三字编码',
  `airport_code_4` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机场四字编码',
  `airport_name_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机场中文名称',
  `airport_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机场英文名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dim_airport
-- ----------------------------
INSERT INTO `dim_airport` VALUES ('北京市', '北京', 'NAY', 'ZBNY', '北京南苑机场', 'NANYUAN');
INSERT INTO `dim_airport` VALUES ('北京市', '北京', 'PEK', 'ZBAA', '北京首都国际机场', 'BEIJING');
INSERT INTO `dim_airport` VALUES ('天津市', '天津', 'TSN', 'ZBTJ', '天津滨海国际机场', 'TIANJIN');
INSERT INTO `dim_airport` VALUES ('河北省', '张家口', 'ZQZ', 'ZBZJ', '张家口宁远机场', 'ZHANGJIAKOU');
INSERT INTO `dim_airport` VALUES ('河北省', '邯郸', 'HDG', 'ZBHD', '邯郸机场', 'HANDAN');
INSERT INTO `dim_airport` VALUES ('河北省', '秦皇岛', 'SHP', 'ZBSH', '秦皇岛山海关机场', 'QINHUANGDAO');
INSERT INTO `dim_airport` VALUES ('河北省', '唐山', 'TVS', 'ZBTS', '唐山三女河机场', 'TANGSHAN');
INSERT INTO `dim_airport` VALUES ('河北省', '邢台', 'XNT', 'ZBXT', '邢台褡裢机场', 'SHAHE');
INSERT INTO `dim_airport` VALUES ('河北省', '石家庄', 'SJW', 'ZBSJ', '石家庄正定国际机场', 'SHIJIAZHUANG');
INSERT INTO `dim_airport` VALUES ('山西省', '太原', 'TYN', 'ZBYN', '太原武宿国际机场', 'TAIYUAN');
INSERT INTO `dim_airport` VALUES ('山西省', '吕梁', 'LLV', 'ZBLL', '吕梁机场', 'LVLIANG');
INSERT INTO `dim_airport` VALUES ('山西省', '长治', 'CIH', 'ZBCZ', '长治王村机场', 'CHANGZHI');
INSERT INTO `dim_airport` VALUES ('山西省', '大同', 'DAT', 'ZBDT', '大同云冈机场', 'DATONG');
INSERT INTO `dim_airport` VALUES ('山西省', '运城', 'YCU', 'ZBYC', '运城机场', 'YUNCHENG');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '阿拉善左旗', 'AXF', '', '阿拉善左旗巴彦浩特机场', 'BAYANHAOTE');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '阿拉善右旗', 'RHT', '', '阿拉善右旗巴丹吉林机场', 'BADANJILIN');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '包头', 'BAV', 'ZBOW', '包头二里半机场', 'BAOTOU');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '赤峰', 'CIF', 'ZBCF', '赤峰玉龙机场', 'CHIFENG');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '鄂尔多斯', 'DSN', 'ZBDS', '鄂尔多斯伊金霍洛机场', 'ORDOS');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '额济纳旗', 'EJN', '', '额济纳旗桃来机场', 'ERJINA');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '二连浩特', 'ERL', 'ZBER', '二连浩特赛乌苏机场', 'ERLIANHOT');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '满洲里', 'NZH', 'ZBMZ', '满洲里西郊机场', 'MANZHOULI');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '乌海', 'WUA', 'ZBUH', '乌海机场', 'WUHAI');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '阿尔山', 'YIE', 'ZBES', '阿尔山伊尔施机场', 'ARXAN');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '巴彦淖尔', 'RLK', 'ZBYZ', '巴彦淖尔天吉泰机场', 'BAYANNUR');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '呼和浩特', 'HET', 'ZBHH', '呼和浩特白塔国际机场', 'HOHHOT');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '呼伦贝尔', 'HLD', 'ZBLA', '呼伦贝尔海拉尔机场', 'HAILAR');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '通辽', 'TGO', 'ZBTL', '通辽机场', 'TONGLIAO');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '乌兰浩特', 'HLH', 'ZBUL', '乌兰浩特机场', 'ULANHOT');
INSERT INTO `dim_airport` VALUES ('内蒙古自治区', '锡林浩特', 'XIL', 'ZBXH', '锡林浩特机场', 'XILINHOT');
INSERT INTO `dim_airport` VALUES ('辽宁省', '鞍山', 'AOG', 'ZYAS', '鞍山腾鳌机场', 'ANSHAN');
INSERT INTO `dim_airport` VALUES ('辽宁省', '长海', 'CNI', 'ZYCH', '长海大长山岛机场', 'CHANGHAI');
INSERT INTO `dim_airport` VALUES ('辽宁省', '朝阳', 'CHG', 'ZYCY', '朝阳机场', 'CHAOYANG');
INSERT INTO `dim_airport` VALUES ('辽宁省', '大连', 'DLC', 'ZYTL', '大连周水子国际机场', 'DALIAN');
INSERT INTO `dim_airport` VALUES ('辽宁省', '丹东', 'DDG', 'ZYDD', '丹东浪头机场', 'DANDONG');
INSERT INTO `dim_airport` VALUES ('辽宁省', '锦州', 'JNZ', 'ZYJZ', '锦州小岭子机场', 'JINZHOU');
INSERT INTO `dim_airport` VALUES ('辽宁省', '沈阳', 'SHE', 'ZYTX', '沈阳桃仙国际机场', 'SHENYANG');
INSERT INTO `dim_airport` VALUES ('辽宁省', '通化', 'TNH', 'ZYTN', '通化三源浦机场', 'TONGHUA');
INSERT INTO `dim_airport` VALUES ('辽宁省', '白山', 'NBS', 'ZYBS', '长白山机场', 'CHANGBAISHAN');
INSERT INTO `dim_airport` VALUES ('辽宁省', '延边', 'YNJ', 'ZYYJ', '延吉朝阳川机场', 'YANJI');
INSERT INTO `dim_airport` VALUES ('辽宁省', '长春', 'CGQ', 'ZYCC', '长春龙嘉国际机场', 'CHANGCHUN');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '鸡西', 'JXA', 'ZYJX', '鸡西兴凯湖机场', 'JIXI');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '佳木斯', 'JMU', 'ZYJM', '佳木斯东郊机场', 'JIAMUSI');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '牡丹江', 'MDG', 'ZYMD', '牡丹江海浪机场', 'MUDANJIANG');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '伊春', 'LDS', 'ZYLD', '伊春林都机场', 'YICHUN');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '黑河', 'HEK', 'ZYHE', '黑河机场', 'HEIHE');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '大庆', 'DQA', 'ZYDQ', '大庆萨尔图机场', 'DAQING');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '大兴安岭', 'JGD', 'ZYJD', '加格达奇机场', 'JIAGEDAQI');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '哈尔滨', 'HRB', 'ZYHB', '哈尔滨太平国际机场', 'HARBIN');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '漠河', 'OHE', 'ZYMH', '漠河古莲机场', 'MOHE');
INSERT INTO `dim_airport` VALUES ('黑龙江省', '齐齐哈尔', 'NDG', 'ZYQQ', '齐齐哈尔三家子机场', 'QIQIHAER');
INSERT INTO `dim_airport` VALUES ('上海市', '上海', 'SHA', 'ZSSS', '上海虹桥国际机场', 'HONGQIAO');
INSERT INTO `dim_airport` VALUES ('上海市', '上海', 'PVG', 'ZSPD', '上海浦东国际机场', 'PUDONG');
INSERT INTO `dim_airport` VALUES ('江苏省', '淮安', 'HIA', 'ZSSH', '淮安涟水机场', 'HUAIAN');
INSERT INTO `dim_airport` VALUES ('江苏省', '南通', 'NTG', 'ZSNT', '南通兴东机场', 'NANTONG');
INSERT INTO `dim_airport` VALUES ('江苏省', '盐城', 'YNZ', 'ZSYN', '盐城南洋机场', 'YANCHENG');
INSERT INTO `dim_airport` VALUES ('江苏省', '扬州', 'YTY', 'ZSYA', '扬州泰州机场', 'YANGZHOU');
INSERT INTO `dim_airport` VALUES ('江苏省', '南京', 'NKG', 'ZSNJ', '南京禄口国际机场', 'NANJING');
INSERT INTO `dim_airport` VALUES ('江苏省', '徐州', 'XUZ', 'ZSXZ', '徐州观音机场', 'XUZHOU');
INSERT INTO `dim_airport` VALUES ('江苏省', '连云港', 'LYG', 'ZSLG', '连云港白塔埠机场', 'LIANYUNGANG');
INSERT INTO `dim_airport` VALUES ('江苏省', '常州', 'CZX', 'ZSCG', '常州奔牛机场', 'CHANGZHOU');
INSERT INTO `dim_airport` VALUES ('江苏省', '无锡', 'WUX', 'ZSWX', '苏南硕放国际机场', 'WUXI');
INSERT INTO `dim_airport` VALUES ('浙江省', '温州', 'WNZ', 'ZSWZ', '温州龙湾国际机场', 'WENZHOU');
INSERT INTO `dim_airport` VALUES ('浙江省', '衢州', 'JUZ', 'ZSJU', '衢州机场', 'QUZHOU');
INSERT INTO `dim_airport` VALUES ('浙江省', '台州', 'HYN', 'ZSLQ', '台州路桥机场', 'TAIZHOU');
INSERT INTO `dim_airport` VALUES ('浙江省', '舟山', 'HSN', 'ZSZS', '舟山普陀山机场', 'ZHOUSHAN');
INSERT INTO `dim_airport` VALUES ('浙江省', '杭州', 'HGH', 'ZSHC', '杭州萧山国际机场', 'HANGZHOU');
INSERT INTO `dim_airport` VALUES ('浙江省', '金华', 'YIW', 'ZSYW', '义乌机场', 'YIWU');
INSERT INTO `dim_airport` VALUES ('浙江省', '宁波', 'NGB', 'ZSNB', '宁波栎社国际机场', 'NINGBO');
INSERT INTO `dim_airport` VALUES ('安徽省', '安庆', 'AQG', 'ZSAQ', '安庆天柱山机场', 'ANQING');
INSERT INTO `dim_airport` VALUES ('安徽省', '池州', 'JUH', 'ZSJH', '池州九华山机场', 'CHIZHOU');
INSERT INTO `dim_airport` VALUES ('安徽省', '黄山', 'TXN', 'ZSTX', '黄山屯溪机场', 'HUANGSHAN');
INSERT INTO `dim_airport` VALUES ('安徽省', '合肥', 'HFE', 'ZSOF', '合肥新桥国际机场', 'HEFEI');
INSERT INTO `dim_airport` VALUES ('安徽省', '阜阳', 'FUG', 'ZSFY', '阜阳西关机场', 'FUYANG');
INSERT INTO `dim_airport` VALUES ('福建省', '福州', 'FOC', 'ZSFZ', '福州长乐国际机场', 'FUZHOU');
INSERT INTO `dim_airport` VALUES ('福建省', '沙县', 'SQJ', '', '三明沙县机场', 'SANMING');
INSERT INTO `dim_airport` VALUES ('福建省', '龙岩', 'LCX', 'ZSLD', '连城冠豸山机场', 'LONGYAN');
INSERT INTO `dim_airport` VALUES ('福建省', '泉州', 'JJN', 'ZSQZ', '泉州晋江机场', 'QUANZHOU');
INSERT INTO `dim_airport` VALUES ('福建省', '南平', 'WUS', 'ZSWY', '武夷山机场', 'WUYISHAN');
INSERT INTO `dim_airport` VALUES ('福建省', '厦门', 'XMN', 'ZSAM', '厦门高崎国际机场', 'XIAMEN');
INSERT INTO `dim_airport` VALUES ('江西省', '赣州', 'KOW', 'ZSGZ', '赣州黄金机场', 'GANZHOU');
INSERT INTO `dim_airport` VALUES ('江西省', '吉安', 'JGS', 'ZSGS', '井冈山机场', 'JINGGANGSHAN');
INSERT INTO `dim_airport` VALUES ('江西省', '宜春', 'YIC', 'ZSYC', '宜春明月山机场', 'YICHUN');
INSERT INTO `dim_airport` VALUES ('江西省', '南昌', 'KHN', 'ZSCN', '南昌昌北国际机场', 'NANCHANG');
INSERT INTO `dim_airport` VALUES ('江西省', '景德镇', 'JDZ', 'ZSJD', '景德镇罗家机场', 'JINGDEZHEN');
INSERT INTO `dim_airport` VALUES ('江西省', '九江', 'JIU', 'ZSJJ', '九江庐山机场', 'JIUJIANG');
INSERT INTO `dim_airport` VALUES ('山东省', '济宁', 'JNG', 'ZSJG', '济宁曲阜机场', 'JINING');
INSERT INTO `dim_airport` VALUES ('山东省', '潍坊', 'WEF', 'ZSWF', '潍坊机场', 'WEIFANG');
INSERT INTO `dim_airport` VALUES ('山东省', '青岛', 'TAO', 'ZSQD', '青岛流亭国际机场', 'QINGDAO');
INSERT INTO `dim_airport` VALUES ('山东省', '济南', 'TNA', 'ZSJN', '济南遥墙国际机场', 'JINAN');
INSERT INTO `dim_airport` VALUES ('山东省', '临沂', 'LYI', 'ZSLY', '临沂沭埠岭机场', 'LINYI');
INSERT INTO `dim_airport` VALUES ('山东省', '烟台', 'YNT', 'ZSYT', '烟台莱山国际机场', 'YANTAI');
INSERT INTO `dim_airport` VALUES ('山东省', '东营', 'DOY', 'ZSDY', '东营胜利机场', 'DONGYING');
INSERT INTO `dim_airport` VALUES ('山东省', '威海', 'WEH', 'ZSWH', '威海国际机场', 'WEIHAI');
INSERT INTO `dim_airport` VALUES ('河南省', '洛阳', 'LYA', 'ZHLY', '洛阳北郊机场', 'LUOYANG');
INSERT INTO `dim_airport` VALUES ('河南省', '郑州', 'CGO', 'ZHCC', '郑州新郑国际机场', 'ZHENGZHOU');
INSERT INTO `dim_airport` VALUES ('河南省', '南阳', 'NNY', 'ZHNY', '南阳姜营机场', 'NANYANG');
INSERT INTO `dim_airport` VALUES ('湖北省', '武汉', 'WUH', 'ZHHH', '武汉天河国际机场', 'WUHAN');
INSERT INTO `dim_airport` VALUES ('湖北省', '宜昌', 'YIH', 'ZHYC', '宜昌三峡机场', 'YICHANG');
INSERT INTO `dim_airport` VALUES ('湖北省', '恩施', 'ENH', 'ZHES', '恩施许家坪机场', 'ENSHI');
INSERT INTO `dim_airport` VALUES ('湖北省', '襄阳', 'XFN', 'ZHXF', '襄阳刘集机场', 'XIANGYANG');
INSERT INTO `dim_airport` VALUES ('湖南省', '常德', 'CGD', 'ZGCD', '常德桃花源机场', 'CHANGDE');
INSERT INTO `dim_airport` VALUES ('湖南省', '怀化', 'HJJ', 'ZGCJ', '怀化芷江机场', 'HUAIHUA');
INSERT INTO `dim_airport` VALUES ('湖南省', '张家界', 'DYG', 'ZGDY', '张家界荷花国际机场', 'ZHANGJIAJIE');
INSERT INTO `dim_airport` VALUES ('湖南省', '长沙', 'CSX', 'ZGHA', '长沙黄花国际机场', 'CHANGSHA');
INSERT INTO `dim_airport` VALUES ('湖南省', '永州', 'LLF', 'ZGLG', '永州零陵机场', 'YONGZHOU');
INSERT INTO `dim_airport` VALUES ('广东省', '东莞', 'DGM', '', '东莞机场', 'DONGGUAN');
INSERT INTO `dim_airport` VALUES ('广东省', '深圳', 'SZX', 'ZGSZ', '深圳宝安国际机场', 'SHENZHEN');
INSERT INTO `dim_airport` VALUES ('广东省', '惠州', 'HUZ', 'ZGHZ', '惠州机场', 'HUIZHOU');
INSERT INTO `dim_airport` VALUES ('广东省', '梅州', 'MXZ', 'ZGMX', '梅县长岗岌机场', 'MEIXIAN');
INSERT INTO `dim_airport` VALUES ('广东省', '揭阳', 'SWA', 'ZGOW', '揭阳潮汕机场', 'JIEYANG');
INSERT INTO `dim_airport` VALUES ('广东省', '湛江', 'ZHA', 'ZGZJ', '湛江机场', 'ZHANJIANG');
INSERT INTO `dim_airport` VALUES ('广东省', '佛山', 'FUO', 'ZGFS', '佛山机场', 'FOSHAN');
INSERT INTO `dim_airport` VALUES ('广东省', '韶关', 'HSC', '', '韶关桂头机场', 'SHAOGUAN');
INSERT INTO `dim_airport` VALUES ('广东省', '珠海', 'ZUH', 'ZGSD', '珠海金湾机场', 'ZHUHAI');
INSERT INTO `dim_airport` VALUES ('广东省', '广州', 'CAN', 'ZGGG', '广州白云国际机场', 'GUANGZHOU');
INSERT INTO `dim_airport` VALUES ('广西壮族自治区', '柳州', 'LZH', 'ZGZH', '柳州白莲机场', 'LIUZHOU');
INSERT INTO `dim_airport` VALUES ('广西壮族自治区', '百色', 'AEB', 'ZGBS', '百色巴马机场', 'BAISE');
INSERT INTO `dim_airport` VALUES ('广西壮族自治区', '北海', 'BHY', 'ZGBH', '北海福成机场', 'BEIHAI');
INSERT INTO `dim_airport` VALUES ('广西壮族自治区', '南宁', 'NNG', 'ZGNN', '南宁吴圩国际机场', 'NANNING');
INSERT INTO `dim_airport` VALUES ('广西壮族自治区', '梧州', 'WUZ', 'ZGWZ', '梧州长洲岛机场', 'WUZHOU');
INSERT INTO `dim_airport` VALUES ('广西壮族自治区', '桂林', 'KWL', 'ZGKL', '桂林两江国际机场', 'GUILIN');
INSERT INTO `dim_airport` VALUES ('海南省', '海口', 'HAK', 'ZJHK', '海口美兰国际机场', 'HAIKOU');
INSERT INTO `dim_airport` VALUES ('海南省', '三亚', 'SYX', 'ZJSY', '三亚凤凰国际机场', 'SANYA');
INSERT INTO `dim_airport` VALUES ('重庆市', '重庆', 'CKG', 'ZUCK', '重庆江北国际机场', 'CHONGQING');
INSERT INTO `dim_airport` VALUES ('重庆市', '万州', 'WXN', 'ZUWX', '万州五桥机场', 'WANZHOU');
INSERT INTO `dim_airport` VALUES ('重庆市', '黔江', 'JIQ', 'ZUQJ', '黔江武陵山机场', 'ZHOUBAI');
INSERT INTO `dim_airport` VALUES ('四川省', '达州', 'DAX', 'ZUDX', '达州河市机场', 'DAZHOU');
INSERT INTO `dim_airport` VALUES ('四川省', '稻城', 'DCY', 'ZUDC', '稻城亚丁机场', 'DAOCHENG');
INSERT INTO `dim_airport` VALUES ('四川省', '九寨沟', 'JZH', 'ZUJZ', '九寨黄龙机场', 'JIUZHAIGOU');
INSERT INTO `dim_airport` VALUES ('四川省', '南充', 'NAO', 'ZUNC', '南充高坪机场', 'NANCHONG');
INSERT INTO `dim_airport` VALUES ('四川省', '攀枝花', 'PZI', 'ZUZI', '攀枝花保安营机场', 'PANZHIHUA');
INSERT INTO `dim_airport` VALUES ('四川省', '成都', 'CTU', 'ZUUU', '成都双流国际机场', 'CHENGDU');
INSERT INTO `dim_airport` VALUES ('四川省', '遂宁', 'SUN', 'ZNSU', '遂宁机场', 'SUINING');
INSERT INTO `dim_airport` VALUES ('四川省', '广元', 'GYS', 'ZUGU', '广元盘龙机场', 'GUANGYUAN');
INSERT INTO `dim_airport` VALUES ('四川省', '康定', 'KGT', 'ZUKD', '甘孜康定机场', 'KANGDING');
INSERT INTO `dim_airport` VALUES ('四川省', '绵阳', 'MIG', 'ZUMY', '绵阳南郊机场', 'MIANYANG');
INSERT INTO `dim_airport` VALUES ('四川省', '泸州', 'LZO', 'ZULZ', '泸州蓝田机场', 'LUZHOU');
INSERT INTO `dim_airport` VALUES ('四川省', '西昌', 'XIC', 'ZUXC', '西昌青山机场', 'XICHANG');
INSERT INTO `dim_airport` VALUES ('四川省', '宜宾', 'YBP', 'ZUYB', '宜宾菜坝机场', 'YIBIN');
INSERT INTO `dim_airport` VALUES ('贵州省', '安顺', 'AVA', 'ZUAS', '安顺黄果树机场', 'ANSHUN');
INSERT INTO `dim_airport` VALUES ('贵州省', '毕节', 'BFJ', 'ZUBJ', '毕节飞雄机场', 'BIJIE');
INSERT INTO `dim_airport` VALUES ('贵州省', '贵阳', 'KWE', 'ZUGY', '贵阳龙洞堡国际机场', 'GUIYANG');
INSERT INTO `dim_airport` VALUES ('贵州省', '黔西南', 'ACX', 'ZUYI', '兴义机场', 'XINGYI');
INSERT INTO `dim_airport` VALUES ('贵州省', '黎平', 'HZH', 'ZUNP', '黎平机场', 'LIPING');
INSERT INTO `dim_airport` VALUES ('贵州省', '黔东南', 'KJH', 'ZUKJ', '凯里黄平机场', 'KAILI');
INSERT INTO `dim_airport` VALUES ('贵州省', '黔南', 'LLB', 'ZULB', '荔波机场', 'LIBO');
INSERT INTO `dim_airport` VALUES ('贵州省', '铜仁', 'TEN', 'ZUTR', '铜仁凤凰机场', 'TONGREN');
INSERT INTO `dim_airport` VALUES ('贵州省', '遵义', 'ZYI', 'ZUZY', '遵义新舟机场', 'ZUNYI');
INSERT INTO `dim_airport` VALUES ('云南省', '普洱', 'SYM', 'ZPSM', '普洱思茅机场', 'PUER');
INSERT INTO `dim_airport` VALUES ('云南省', '腾冲', 'TCZ', 'ZUTC', '腾冲驼峰机场', 'TENGCHONG');
INSERT INTO `dim_airport` VALUES ('云南省', '临沧', 'LNJ', 'ZPLC', '临沧机场', 'LINCANG');
INSERT INTO `dim_airport` VALUES ('云南省', '西双版纳', 'JHG', 'ZPJH', '西双版纳嘎洒国际机场', 'JINGHONG');
INSERT INTO `dim_airport` VALUES ('云南省', '丽江', 'LJG', 'ZPLJ', '丽江三义机场', 'LIJIANG');
INSERT INTO `dim_airport` VALUES ('云南省', '昭通', 'ZAT', 'ZPZT', '昭通机场', 'ZHAOTONG');
INSERT INTO `dim_airport` VALUES ('云南省', '隆阳', 'BSD', 'ZPBS', '保山云瑞机场', 'BAOSHAN');
INSERT INTO `dim_airport` VALUES ('云南省', '大理', 'DLU', 'ZPDL', '大理机场', 'DALIXIAGUAN');
INSERT INTO `dim_airport` VALUES ('云南省', '德宏', 'LUM', 'ZPMS', '德宏芒市机场', 'MANGSHI');
INSERT INTO `dim_airport` VALUES ('云南省', '迪庆', 'DIG', 'ZPDQ', '迪庆香格里拉机场', 'DIQING');
INSERT INTO `dim_airport` VALUES ('云南省', '昆明', 'KMG', 'ZPPP', '昆明长水国际机场', 'KUNMING');
INSERT INTO `dim_airport` VALUES ('云南省', '文山', 'WNH', 'ZPWS', '文山普者黑机场', 'WENSHAN');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '阿里', 'NGQ', 'ZUAL', '阿里昆莎机场', 'SHIQUANHE');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '昌都', 'BPX', 'ZUBD', '昌都邦达机场', 'CHAMDO');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '日喀则', 'RKZ', 'ZURK', '日喀则和平机场', 'RIKAZE');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '拉萨', 'LXA', 'ZULS', '拉萨贡嘎机场', 'LHASA');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '林芝', 'LZY', 'ZUNZ', '林芝米林机场', 'NYINGCHI');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '陕西省', '', '', '', '');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '西安', 'XIY', 'ZLXY', '西安咸阳国际机场', 'XIAN');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '榆林', 'UYN', 'ZLYL', '榆林榆阳机场', 'YULIN');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '汉中', 'HZG', 'ZLHZ', '汉中西关机场', 'HANZHONG');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '安康', 'AKA', 'ZLAK', '安康五里铺机场', 'ANKANG');
INSERT INTO `dim_airport` VALUES ('西藏自治区', '延安', 'ENY', 'ZLYA', '延安二十里堡机场', 'YANAN');
INSERT INTO `dim_airport` VALUES ('甘肃省', '甘南', 'GXH', 'ZLXH', '甘南夏河机场', 'XIAHE');
INSERT INTO `dim_airport` VALUES ('甘肃省', '金昌', 'JIC', 'ZLJC', '金昌金川机场', 'JINCHANG');
INSERT INTO `dim_airport` VALUES ('甘肃省', '张掖', 'YZY', 'ZLZY', '张掖甘州机场', 'ZHANGYE');
INSERT INTO `dim_airport` VALUES ('甘肃省', '敦煌', 'DNH', 'ZLDH', '敦煌机场', 'DUNHUANG');
INSERT INTO `dim_airport` VALUES ('甘肃省', '嘉峪关', 'JGN', 'ZLJQ', '嘉峪关机场', 'JIAYUGUAN');
INSERT INTO `dim_airport` VALUES ('甘肃省', '兰州', 'LHW', 'ZLLL', '兰州中川机场', 'LANZHOU');
INSERT INTO `dim_airport` VALUES ('甘肃省', '庆阳', 'IQN', 'ZLQY', '庆阳机场', 'QINGYANG');
INSERT INTO `dim_airport` VALUES ('甘肃省', '天水', 'THQ', 'ZLTS', '天水麦积山机场', 'TIANSHUI');
INSERT INTO `dim_airport` VALUES ('青海省', '西宁', 'XNN', 'ZLXN', '西宁曹家堡机场', 'XINING');
INSERT INTO `dim_airport` VALUES ('青海省', '格尔木', 'GOQ', 'ZLGM', '格尔木机场', 'GOLMUD');
INSERT INTO `dim_airport` VALUES ('青海省', '玉树', 'YUS', 'ZLYS', '玉树巴塘机场', 'YUSHU');
INSERT INTO `dim_airport` VALUES ('宁夏回族自治区', '固原', 'GYU', 'ZLGY', '固原六盘山机场', 'GUYUAN');
INSERT INTO `dim_airport` VALUES ('宁夏回族自治区', '银川', 'INC', 'ZLIC', '银川河东国际机场', 'YINCHUAN');
INSERT INTO `dim_airport` VALUES ('宁夏回族自治区', '中卫', 'ZHY', 'ZLZW', '中卫沙坡头机场', 'ZHONGWEI');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '阿克苏', 'AKU', 'ZWAK', '阿克苏机场', 'AKSU');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '阿勒泰', 'AAT', 'ZWAT', '阿勒泰机场', 'ALTAY');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '富蕴', 'FYN', 'ZWFY', '富蕴机场', 'FUYUN');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '哈密', 'HMI', 'ZWHM', '哈密机场', 'HAMI');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '和田', 'HTN', 'ZWTN', '和田机场', 'HETIAN');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '库尔勒', 'KRL', 'ZWKL', '库尔勒机场', 'KORLA');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '且末', 'IQM', 'ZWCM', '且末机场', 'QIEMO');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '吐鲁番', 'TLQ', 'ZWTP', '吐鲁番交河机场', 'TULUFAN');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '乌鲁木齐', 'URC', 'ZWWW', '乌鲁木齐地窝堡国际机场', 'URUMQI');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '博尔塔拉', 'BPL', 'ZWBL', '博乐阿拉山口机场', 'BOLE');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '布尔津', 'KJI', 'ZWKN', '布尔津喀纳斯机场', 'BUERJIN');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '喀什', 'KHG', 'ZWSH', '喀什机场', 'KASHGAR');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '克拉玛依', 'KRY', 'ZWKM', '克拉玛依机场', 'KARAMAY');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '库车', 'KCA', 'ZWKC', '库车龟兹机场', 'KUQA');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '塔城', 'TCG', 'ZWTC', '塔城机场', 'TACHENG');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '伊宁', 'YIN', 'ZWYN', '伊宁机场', 'YINING');
INSERT INTO `dim_airport` VALUES ('新疆维吾尔自治区', '新源', 'NLT', 'ZWNL', '新源那拉提机场', 'NALATI');
INSERT INTO `dim_airport` VALUES ('香港特别行政区', '香港', 'HKG', 'VHHH', '香港国际机场', 'HONG KONG');
INSERT INTO `dim_airport` VALUES ('澳门特别行政区', '澳门', 'MFM', 'VMMC', '澳门国际机场', 'MACAU');
INSERT INTO `dim_airport` VALUES ('台湾省', '台北', 'TSA', 'RCSS', '台北松山机场', 'TAIBEI');
INSERT INTO `dim_airport` VALUES ('台湾省', '桃园', 'TPE', 'RCTP', '桃园国际机场', 'TAOYUAN');
INSERT INTO `dim_airport` VALUES ('台湾省', '高雄', 'KHH', 'RCKH', '高雄国际机场', 'GAOXIONG');

SET FOREIGN_KEY_CHECKS = 1;
