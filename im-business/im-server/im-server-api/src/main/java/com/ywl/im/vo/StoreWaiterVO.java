package com.ywl.im.vo;


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
@ApiModel("店铺客服VO")
public class StoreWaiterVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("店铺id")
    private Long storeId;
    @ApiModelProperty("客服id 接入系统提供")
    private String waiterId;
    @ApiModelProperty("客服昵称")
    private String waiterNickname;
    @ApiModelProperty("客服头像")
    private String waiterAvatar;
    @ApiModelProperty("客服类型")
    private String type;
}
