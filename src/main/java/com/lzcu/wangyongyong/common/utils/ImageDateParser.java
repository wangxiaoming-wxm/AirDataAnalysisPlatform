package com.lzcu.wangyongyong.common.utils;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.Serializable;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;


/**
 * java图像识别
 */
public class ImageDateParser implements Serializable {
    public static String getImgText(String imageLocation) {
        ITesseract instance = new Tesseract();
        instance.setDatapath("D:\\software\\Tesseract-OCR\\tessdata\\");
        instance.setLanguage("eng");
        ImageIO.scanForPlugins();//解决此类问题： Unsupported image format. May need to install JAI Image I/O package.
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
        String imgPath = FileUtil.getNetUrlHttp("https://api.133.cn/third/textImg?code=FKoJ2OAUjurED6z8oTx1DA==");
        System.out.println(getImgText(imgPath));
    }

}
