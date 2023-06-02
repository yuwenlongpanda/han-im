package com.ywl.framework.gateway;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TestController {

    @GetMapping("test/empty")
    public Boolean test(Boolean flag) {
        return Optional.ofNullable(flag).orElse(Boolean.TRUE);
    }
}
