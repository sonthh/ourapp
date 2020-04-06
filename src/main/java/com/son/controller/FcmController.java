package com.son.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.son.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping()
@Validated
public class FcmController {

    private final FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @GetMapping()
    public String fcm() {
        return "client/index";
    }

    @GetMapping("fcm/{userId}")
    @ResponseBody
    public String fcmTest(@PathVariable Integer userId) throws JsonProcessingException {
        fcmService.sendToTopic("user-" + userId, "Sơn Đẹp Trai", "Tôi là Sơn Đẹp Trai");
        return "ok";
    }

}
