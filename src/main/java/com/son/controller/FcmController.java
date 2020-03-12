package com.son.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.son.entity.Product;
import com.son.entity.ProductStatus;
import com.son.service.FcmService;
import com.son.service.SocketIOService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
@Validated
public class FcmController {

    @GetMapping()
    public String fcm() {
        return "client/index";
    }

}