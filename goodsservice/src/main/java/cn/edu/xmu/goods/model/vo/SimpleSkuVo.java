package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SimpleSkuVo {

    @ApiModelProperty("skuId")
    private Long id;

    @ApiModelProperty("sku名字")
    private String name;

    @ApiModelProperty("sku编号")
    private String skuSn;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("库存")
    private Integer inventory;

    @ApiModelProperty("原价")
    private Integer originalPrice;

    @ApiModelProperty("现价")
    private Integer price;

    @ApiModelProperty("是否被逻辑删除")
    private Boolean disable;
}
