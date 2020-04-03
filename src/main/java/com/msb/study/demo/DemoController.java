package com.msb.study.demo;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月19日 23:36:52
**/
@RestController
public class DemoController {
    @Autowired
    private Configuration conf;

    @GetMapping("createFile")
    public String createFile() throws Exception {
        int id = 101;
        File htmlFile = new File("c:/cccc/" + id + ".html");

        if(!htmlFile.exists()) {
            //如果文件不存在
            Template template = conf.getTemplate("girl.tpl");
            htmlFile.createNewFile();
            //写入数据
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));
            HashMap<Object, Object> map = new HashMap<>();

            Girl girl = new Girl();
            girl.setName("lucy");
            girl.setAge(18);
            map.put("girl", girl);
            template.process(map, writer);
        }

        return "ok";
    }
}