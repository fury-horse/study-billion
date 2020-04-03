package com.msb.study.model;


import lombok.Data;

import java.util.Date;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月25日 17:33:33
**/
@Data
public class Item {
    private Integer id;
    private String title;
    private String content;
    private Date lastGenerate;
}