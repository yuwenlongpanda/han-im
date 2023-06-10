package com.ywl.webapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywl.data.api.entity.Message;
import com.ywl.data.api.mapper.MessageMapper;
import com.ywl.webapi.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * @author yanyuluji
 * @description 针对表【message】的数据库操作Service实现
 * @createDate 2023-06-08 21:47:01
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


}
