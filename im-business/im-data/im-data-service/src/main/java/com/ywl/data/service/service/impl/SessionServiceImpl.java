package com.ywl.data.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywl.data.api.entity.Session;
import com.ywl.data.api.mapper.SessionMapper;
import com.ywl.data.api.service.SessionService;
import org.springframework.stereotype.Service;

/**
 * @author yanyuluji
 * @description 针对表【session】的数据库操作Service实现
 * @createDate 2023-06-10 10:32:34
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session>
        implements SessionService {

}
