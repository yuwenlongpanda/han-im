package com.ywl.framework.web.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.fastjson.JSON;
import com.ywl.framework.web.result.ResultWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ywl.framework.web.result.BaseResultCodeEnum.BLOCK_EXCEPTION;

/**
 * @author liao
 */
@Component
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        ResultWrapper<Object> fail = ResultWrapper.fail(BLOCK_EXCEPTION);
        httpServletResponse.setStatus(200);
        httpServletResponse.setHeader("content-type", "application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(fail));
    }
}
