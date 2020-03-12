package com.son.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.son.entity.Product;
import com.son.entity.ProductStatus;
import com.son.service.FcmService;
import com.son.service.SocketIOService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Validated
public class SocketIOTestController {
    private final SocketIOService socketIOService;

    private final FcmService fcmService;

    public SocketIOTestController(SocketIOService socketIOService, FcmService fcmService) {
        this.socketIOService = socketIOService;
        this.fcmService = fcmService;
    }

    @GetMapping("1")
    public void test() {
        Product data = new Product();
        data.setId(333);
        data.setPrice(444);
        data.setStatus(ProductStatus.AVAILABLE);
        data.setName("kakakak");
        socketIOService.sendToRoom("user-2", "NOTIFICATION", data);
    }

    @GetMapping("3")
    public void test3() throws JsonProcessingException {
        fcmService.sendToTopic("user-2", "hello", "kakka");
    }
}