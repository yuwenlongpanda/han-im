package com.ywl.im.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 店铺配置
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@ApiModel("更新店铺DTO")
public class UpdateStoreConfigDTO  {
    @ApiModelProperty("店铺名称")
    private String name;
    @ApiModelProperty("店铺头像")
    private String avatar;
    @ApiModelProperty("系统id")
    private Integer systemId;
}
