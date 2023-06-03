
package com.ywl.im.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺下的客服
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@ApiModel("转移客服会话DTO")
public class TransferWaiterDTO {
    @ApiModelProperty(value = "店铺id",required = true)
    private Long storeId;
    @ApiModelProperty(value = "会话id",required = true)
    private Long sessionId;
    @ApiModelProperty(value = "接收转移的客服id",required = true)
    private String toWaiterId;
    @ApiModelProperty("转移原因")
    private String reason;
    @ApiModelProperty(value = "发起转移的客服id", hidden = true)
    private String currentWaiterId;
    @ApiModelProperty(value = "系统id", hidden = true)
    private Integer systemId;
}
