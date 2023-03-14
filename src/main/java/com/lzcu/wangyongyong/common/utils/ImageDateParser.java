package com.lzcu.wangyongyong.common.utils;
import java.io.File;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


/**
 * java图像识别
 */
public class ImageDateParser {
    public static String getImgText(String imageLocation) {
        ITesseract instance = new Tesseract();
        instance.setDatapath("D:\\software\\Tesseract-OCR\\tessdata\\");
        instance.setLanguage("eng");

        try
        {
            String imgText = instance.doOCR(new File(imageLocation));
            return imgText;
        }
        catch (TesseractException e)
        {
            e.getMessage();
            return "Error while reading image";
        }
    }


    public static void main(String[] args) {
        String imgPath = FileUtil.getNetUrlHttp("https://api.133.cn/third/textImg?code=8rqIyZaVrgfO4W80%2B9I4Gg==");
        System.out.println(getImgText(imgPath));
    }

}
