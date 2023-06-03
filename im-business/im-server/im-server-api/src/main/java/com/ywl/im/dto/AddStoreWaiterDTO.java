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
@ApiModel("新增客服DTO")
public class AddStoreWaiterDTO {
    @ApiModelProperty("接入系统客服id")
    private String waiterId;
    @ApiModelProperty("接入系统客服昵称")
    private String waiterNickname;
    @ApiModelProperty("接入系统客服头像")
    private String waiterAvatar;
    @ApiModelProperty("接入系统客服类型 接入系统提供")
    private String type;
    @ApiModelProperty("系统id api调用时使用该参数")
    private Integer systemId;
    @ApiModelProperty("店铺id im中台后管调用时使用该参数")
    private Long storeId;
}
