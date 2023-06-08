package com.ywl.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywl.data.entity.Message;
import com.ywl.framework.common.model.ImMqMessage;

/**
* @author yanyuluji
* @description 针对表【message】的数据库操作Service
* @createDate 2023-06-08 21:47:01
*/
public interface MessageService extends IService<Message> {

    void save(ImMqMessage mqMessage);

}
