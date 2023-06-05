package com.ywl.product.controller;


import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.product.mq.producer.ImMqProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消息接口")
@RestController
@Slf4j
@RequestMapping("message")
public class MessageController {

    @Autowired
    private ImMqProducerService producerService;

    @ApiOperation(value = "发送消息", notes = "发送消息", httpMethod = "POST")
    @PostMapping("send")
    public Boolean send(@RequestBody ImMqMessage imMqMessage) {
        producerService.sendMq(imMqMessage);
        return true;
    }

}

