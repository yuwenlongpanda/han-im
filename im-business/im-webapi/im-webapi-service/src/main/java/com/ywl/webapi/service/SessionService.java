package com.ywl.webapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywl.data.api.entity.Session;
import com.ywl.webapi.model.query.ConversationQuery;
import com.ywl.webapi.model.vo.ConversationVo;

import java.util.List;

/**
* @author yanyuluji
* @description 针对表【session】的数据库操作Service
* @createDate 2023-06-10 10:32:34
*/
public interface SessionService extends IService<Session> {

    List<ConversationVo> latestConversations(ConversationQuery conversationQuery);

}
