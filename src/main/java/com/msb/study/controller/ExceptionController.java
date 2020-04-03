package com.msb.study.controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月27日 14:13:26
**/
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        e.printStackTrace();
        model.addAttribute("e", e);
        return "error";
    }
}