package com.ywl.webapi.controller;


import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.webapi.mq.producer.ImMqProducerService;
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
    public void send(@RequestBody ImMqMessage imMqMessage) {
        producerService.sendMq(imMqMessage);
    }

    @ApiOperation(value = "查询历史消息", notes = "查询历史消息", httpMethod = "POST")
    @PostMapping("loadHistory")
    public void loadHistory() {
    }

    @ApiOperation(value = "设置私聊已读", notes = "设置私聊已读", httpMethod = "POST")
    @PostMapping("mark")
    public void markMessageAsRead() {
    }

    @ApiOperation(value = "撤回消息", notes = "撤回消息", httpMethod = "POST")
    @PostMapping("recall")
    public void recallMessage() {
    }

    @ApiOperation(value = "删除消息", notes = "删除消息", httpMethod = "POST")
    @PostMapping("delete")
    public void deleteMessage() {
    }

}

