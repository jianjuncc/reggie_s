package com.reggie.controller;

import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    String path;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info("文件名称{}", file.toString());
        //获取原始字段名
        String originalFilename = file.getOriginalFilename();
        // 获取后缀
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新重新命名
        String filename = UUID.randomUUID().toString();

        //全新的文件名
        String newFile = filename + suffix;
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        file.transferTo(new File(path + newFile));
        return R.success(newFile);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //输入读取图片
            FileInputStream inputStream = new FileInputStream(path + name);
            //输入流写入数据
            int len;
            byte[] bytes = new byte[1024];
            ServletOutputStream outputStream = response.getOutputStream();
            while ((len = inputStream.read(bytes))  != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
