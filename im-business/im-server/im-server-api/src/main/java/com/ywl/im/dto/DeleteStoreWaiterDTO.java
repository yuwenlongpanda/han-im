package com.ywl.im.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 删除店铺客服DTO
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@ApiModel("删除客服DTO")
public class DeleteStoreWaiterDTO {
    /**
     * 客服id集合
     */
    @ApiModelProperty("接入系统提供的客服ids")
    private List<String> waiterIds;
    @ApiModelProperty("系统id api调用时使用该参数")
    private Integer systemId;
    @ApiModelProperty("店铺id im中台后管调用时使用该参数")
    private Long storeId;
}
