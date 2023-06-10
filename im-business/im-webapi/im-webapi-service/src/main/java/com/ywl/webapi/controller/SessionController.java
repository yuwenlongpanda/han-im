package com.ywl.webapi.controller;

import com.ywl.webapi.model.query.ConversationQuery;
import com.ywl.webapi.model.vo.ConversationVo;
import com.ywl.webapi.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "会话接口")
@RestController
@RequestMapping("session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @ApiOperation(value = "加载会话列表", notes = "加载会话列表", httpMethod = "POST")
    @PostMapping("latest")
    public List<ConversationVo> latestConversations(@RequestBody ConversationQuery conversationQuery) {
        return sessionService.latestConversations(conversationQuery);
    }

    @ApiOperation(value = "置顶/取消置顶", notes = "置顶/取消置顶", httpMethod = "POST")
    @PostMapping("top")
    public void topConversation(@RequestBody ConversationQuery conversationQuery) {
    }

    @ApiOperation(value = "删除会话", notes = "删除会话", httpMethod = "POST")
    @PostMapping("delete")
    public void deleteConversation(@RequestBody ConversationQuery conversationQuery) {
    }

}

