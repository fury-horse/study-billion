package com.msb.study.model;


import lombok.Data;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月25日 22:23:55
**/
@Data
public class ItemHtml extends Item {
    // 生成文件的状态，有没有生成成功。
    private String htmlStatus;
    private String location;
}