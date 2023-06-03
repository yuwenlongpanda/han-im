package com.ywl.im.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private String code;
    private String message;
    private Object data;
}
