package com.son.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
@Validated
public class FcmController {

    @GetMapping()
    public String fcm() {
        return "client/index";
    }

}