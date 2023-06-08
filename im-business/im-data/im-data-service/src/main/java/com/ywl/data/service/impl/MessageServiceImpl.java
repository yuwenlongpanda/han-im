package com.ywl.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywl.data.entity.Message;
import com.ywl.data.mapper.MessageMapper;
import com.ywl.data.service.MessageService;
import com.ywl.framework.common.model.ImMqMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanyuluji
 * @description 针对表【message】的数据库操作Service实现
 * @createDate 2023-06-08 21:47:01
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    @Transactional
    public void save(ImMqMessage mqMessage) {
        List<Message> messageList = new ArrayList<>();

        List<String> toIds = mqMessage.getToIds();
        for (String id : toIds) {
            Message message = new Message();
            message.setFromId(mqMessage.getFromId());
            // TODO
            message.setMessageIndex(1);
            message.setSessionId(Integer.valueOf(id));
            message.setType(1);
            message.setPayload(mqMessage.getContent());
            // TODO
            message.setCreateTimeStamp(mqMessage.getCreateTimeStamp());
            message.setIsDeleted(0);

            messageList.add(message);
        }
        saveBatch(messageList);
    }

}
