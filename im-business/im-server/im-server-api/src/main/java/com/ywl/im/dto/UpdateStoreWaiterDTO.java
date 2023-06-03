
package com.ywl.im.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * 店铺下的客服
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@ApiModel("更新客服信息DTO")
public class UpdateStoreWaiterDTO {

    @ApiModelProperty("客服id 接入系统提供")
    private String waiterId;
    @ApiModelProperty("客服昵称")
    private String waiterNickname;
    @ApiModelProperty("客服头像")
    private String waiterAvatar;
    @ApiModelProperty("系统id api调用时使用该参数")
    private Integer systemId;
    @ApiModelProperty("店铺id im中台后管调用时使用该参数")
    private Long storeId;
}
