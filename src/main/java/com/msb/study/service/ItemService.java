package com.msb.study.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.msb.study.mapper.ItemDAO;
import com.msb.study.model.Item;
import com.msb.study.model.ItemExample;
import com.msb.study.model.ItemHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月25日 18:04:13
**/
@Service
public class ItemService {
    @Autowired
    private ItemDAO itemDAO;
    @Value(value = "${nginx.html.root}")
    String htmlRoot;
    @Value(value = "${jfinal.templates.location}")
    String templatesLocation;

    public int insert(Item item) {
        return itemDAO.insert(item);
    }

    public List<Item> selectAll() {
        return itemDAO.selectByExample(new ItemExample());
    }

    public String getFileTemplateString() throws Exception {
        //URL url = this.getClass().getClassLoader().getResource("templates/item.html");

        String file = templatesLocation + "item.html";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        // 读缓冲区
        StringBuffer sb = new StringBuffer();

        String lineStr = reader.readLine();
        while (lineStr!=null) {
            sb.append(lineStr).append("\r\n");
            lineStr = reader.readLine();
        }
        reader.close();

        return sb.toString();
    }

    public boolean saveFileTemplateString(String content) throws Exception {
        String file = templatesLocation + "item.html";

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        writer.close();
        return true;
    }

    public Item selectById(int id) {
        return itemDAO.selectByPrimaryKey(id);
    }

    public boolean generateHtml(int id) throws Exception {
        // 初始化模板引擎
        Engine engine = JFinalViewResolver.engine;

        // 从数据源，获取数据
        Item item = itemDAO.selectByPrimaryKey(id);
        //c:/dev/uploads/

        // 前端模板用的键值对
        Kv kv = Kv.by("item", item);

        // 文件写入路径
        String fileName = "item"+id+".html";
        String filePath = htmlRoot;
        // 路径 直接能被用户访问
        File file = new File(filePath+fileName);

        // 开始渲染 输出文件
        com.jfinal.template.Template template = engine.getTemplate("item.html");
        template.render(kv, file);
        return true;
    }

    public List<ItemHtml> generateAll() {
        List<ItemHtml> list = itemDAO.selectAllByItemHtml(new ItemExample());

        // 初始化模板引擎
        Engine engine = JFinalViewResolver.engine;
        // 文件写入路径

        String filePath = htmlRoot;
        // 获取模板
        com.jfinal.template.Template template = engine.getTemplate("item.html");

        for (ItemHtml item : list) {

            Kv kv = Kv.by("item", item);
            // 路径 直接能被用户访问
            String fileName = "item"+item.getId()+".html";
            File file = new File(filePath+fileName);

            try {
                // 开始渲染 输出文件
                template.render(kv, file);
                item.setHtmlStatus("ok");
                item.setLocation(htmlRoot + fileName);
            }catch (Exception e) {
                // 记日志
                item.setHtmlStatus("err");
                continue;
            }
        }

        return list;
    }

    public boolean generatemain() {
        // 初始化模板引擎
        Engine engine = JFinalViewResolver.engine;

        // 从数据源，获取数据
        List<Item> items = itemDAO.selectByExample(new ItemExample());
        //c:/dev/uploads/

        // 前端模板用的键值对
        Kv kv = Kv.by("items", items);

        // 文件写入路径
        String fileName = "main.html";
        String filePath = htmlRoot;
        // 路径 直接能被用户访问
        File file = new File(filePath+fileName);

        // 开始渲染 输出文件
        Template template = engine.getTemplate("item_main.html");
        template.render(kv, file);

        return true;
    }

    public HashMap<String, Boolean> health() throws Exception {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("192.168.150.113", null);
        map.put("192.168.150.213", null);
        map.put("192.168.150.133", null);

        for (String key : map.keySet()) {
            InetAddress inetAddress = InetAddress.getByName(key);
            boolean reachable = inetAddress.isReachable(3000);
            map.put(key, reachable);
        }
        return map;
    }

    private static List<Integer> locks = new ArrayList<>();

    public boolean getLock(int id) {
        int index = locks.indexOf(id);
        if (index != -1) {
            return false;
        }

        locks.add(id);
        return true;
    }

    public boolean add(Item item) {
        try {
            //1.写入数据库
            insert(item);
            //2.生成静态文件
            generateHtml(item.getId());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new RuntimeException("添加Item失败");
        }

        return true;
    }

    public List<Item> checkFile() {
        List<Item> items = itemDAO.selectByExample(new ItemExample());

        for (Iterator<Item> iter = items.iterator(); iter.hasNext(); ) {
            Item item = iter.next();

            // 文件写入路径
            String fileName = "item"+item.getId()+".html";
            String filePath = htmlRoot;
            // 路径 直接能被用户访问
            File file = new File(filePath+fileName);
            if (file.exists()) {
                iter.remove();
            }
        }
        return items;
    }

    public void generateCategory() {
        // 静态文件页数
        int staticPage = 2;
        // 每页显示多少
        int pageSize = 5;

        ItemExample example = new ItemExample();
        example.setLimit(staticPage * pageSize);

        // 初始化模板引擎
        Engine engine = JFinalViewResolver.engine;
        List<Item> list = itemDAO.selectByExample(example);


        for (int i=1; i<=staticPage; i++) {
            int bgn = (i-1) * pageSize;
            int end = i * pageSize;
            List<Item> subList = list.subList(bgn>list.size() ? list.size() : bgn, end>list.size() ? list.size() : end);

            // 前端模板用的键值对
            Kv kv = Kv.by("items", subList);
            //文件写入路径
            String filePath = htmlRoot;
            String fileName = "list_" + i + ".html";

            File file = new File(filePath + fileName);
            Template template = engine.getTemplate("item_page.html");
            template.render(kv, file);
        }
    }

    public Page<Item> findByPage(int pageNum, int pageSize) {
        // TODO Auto-generated method stub

        Page<Item> page = PageHelper.startPage(pageNum, pageSize);

        ItemExample example = new ItemExample();
        itemDAO.selectByExample(example);

        return page;
    }
}