package com.lzcu.wangyongyong;

import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriterSpi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

/**
 * @author 王勇勇
 * @Date 2023年2月28日16:10:56
 */
@SpringBootApplication
@MapperScan("com.lzcu.wangyongyong.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
