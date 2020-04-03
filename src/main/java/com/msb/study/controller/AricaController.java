package com.msb.study.controller;


import com.github.pagehelper.Page;
import com.msb.study.model.Item;
import com.msb.study.model.ItemHtml;
import com.msb.study.service.ItemService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月25日 14:01:23
**/
@Controller
public class AricaController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("")
    public String index() {
        System.out.println("arica");
        return "arica";
    }

    @RequestMapping("addtor")
    public String addtor() {
        System.out.println("addtor");
        return "add";
    }

    @RequestMapping(value = "editor")
    public String editor(Model model, int id) {
        Item item = itemService.selectById(id);
        Boolean canWrite = itemService.getLock(id);

        model.addAttribute("canWrite", canWrite);
        model.addAttribute("item", item);
        return "editor";
    }

    @RequestMapping(value = "add")
    public String add(Item item, Model model) {
        System.out.println("add");
        itemService.add(item);

        model.addAttribute("msg", "添加商品成功, <a href = 'view?id="
                +item.getId()+"' target='_blank' class=\"layui-btn\">预览一下</a>");

        return "success";
    }

    @RequestMapping(value = "view")
    public String view(Model model, int id) {
        Item item = itemService.selectById(id);
        model.addAttribute("item", item);
        return "item";
    }

    @RequestMapping(value = "itemList")
    public String itemList(Model model) {
        List<Item> list = itemService.selectAll();
        model.addAttribute("items", list);
        return "item_list";
    }

    @RequestMapping(value = "templates")
    public String templates() {
        return "templates";
    }

    @RequestMapping(value = "editTemplate")
    public String editTemplate(Model model) throws Exception {
        String tplStr = itemService.getFileTemplateString();
        model.addAttribute("tplStr", tplStr);
        return "edit_template";
    }

    @RequestMapping(value = "saveTemplate")
    public String saveTemplate(Model model, String content) throws Exception {
        itemService.saveFileTemplateString(content);
        model.addAttribute("msg", "保存成功");
        return "success";
    }

    @RequestMapping(value = "generate")
    public String generate(Model model, int id) throws Exception {
        itemService.generateHtml(id);
        String msg = "文件生成成功，<a href='item"+id+".html' target='_blank'>查看</a>";
        model.addAttribute("msg", msg);
        return "success";
    }

    @RequestMapping(value = "generateAll")
    public String generateAll(Model model) {
        List<ItemHtml> list = itemService.generateAll();
        model.addAttribute("result", list);
        return "generateAll";
    }

    // 电商分类页
    @RequestMapping("main")
    public String main(Model model,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5")int pageSize) {

        // category 参数没穿
        Page<Item> items = itemService.findByPage(pageNum,pageSize);


        System.out.println(ToStringBuilder.reflectionToString(items));

        model.addAttribute("items", items);
        return "item_page";
    }

    @RequestMapping(value = "generateMain")
    public String generateMain(Model model) {
        itemService.generatemain();
        String msg = "文件生成成功，<a href='main.html' target='_blank'>查看</a>";
        model.addAttribute("msg", msg);
        return "success";
    }

    // 生成电商系统首页html
    @RequestMapping("generateCategory")
    public String generateCategory(Model model) {

        itemService.generateCategory();
        String msg = "文件生成成功，<a href='list_1.html' target='_blank'>查看</a>";
        model.addAttribute("msg", msg);
        return "success";
    }

    @RequestMapping(value = "health")
    public String health(Model model) throws Exception {
        HashMap<String, Boolean> map = itemService.health();
        model.addAttribute("map", map);
        return "health";
    }

    @RequestMapping(value = "check")
    public String check(Model model) {
        List<Item> errorList = itemService.checkFile();
        model.addAttribute("errorList", errorList);
        return "check";
    }
}