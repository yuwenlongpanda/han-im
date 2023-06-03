package com.ywl.im.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺下的客服
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("客服查询DTO")
public class StoreWaiterDTO extends PageDTO {
    @ApiModelProperty("接入系统提供的客服id")
    private String waiterId;
    @ApiModelProperty("客服昵称")
    private String waiterNickname;
    @ApiModelProperty("系统id api调用时使用该参数")
    private Integer systemId;
    @ApiModelProperty("店铺id im中台后管调用时使用该参数")
    private Long storeId;
}
