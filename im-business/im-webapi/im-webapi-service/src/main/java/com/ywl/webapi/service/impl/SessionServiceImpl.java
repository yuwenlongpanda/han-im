package com.ywl.webapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywl.data.api.entity.Session;
import com.ywl.data.api.mapper.SessionMapper;
import com.ywl.webapi.model.query.ConversationQuery;
import com.ywl.webapi.model.vo.ConversationVo;
import com.ywl.webapi.service.SessionService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanyuluji
 * @description 针对表【session】的数据库操作Service实现
 * @createDate 2023-06-10 10:32:34
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    public List<ConversationVo> latestConversations(ConversationQuery conversationQuery) {
        Integer nextConversation = conversationQuery.getNextConversation();
        Integer count = conversationQuery.getCount();

        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("create_user", nextConversation);
        if (nextConversation != null && nextConversation > 0) {
            queryWrapper.gt("id", nextConversation);
        }
        queryWrapper.last(" limit " + count);
        List<Session> sessionList = sessionMapper.selectList(queryWrapper);

        List<ConversationVo> res = new ArrayList<>();
        for (Session session : sessionList) {
            ConversationVo conversationVo = new ConversationVo();
            try {
                BeanUtils.copyProperties(conversationVo, session);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.add(conversationVo);
        }
        return res;
    }
}
