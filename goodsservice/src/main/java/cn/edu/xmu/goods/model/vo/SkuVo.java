package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品sku详细信息
 */
@Data
public class SkuVo {

    @ApiModelProperty("skuId")
    private Long id;

    @ApiModelProperty("goodsSpuId")
    private Long goodsSpuId;

    @ApiModelProperty("sku编号")
    private String skuSn;

    @ApiModelProperty("sku名字")
    private String name;

    @ApiModelProperty("原价")
    private Long originalPrice;

    @ApiModelProperty("配置参数json")
    private String configuration;

    @ApiModelProperty("重量")
    private Long weight;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("库存")
    private Integer inventory;

    @ApiModelProperty("详细描述")
    private String detail;

    @ApiModelProperty("是否被删除")
    private Boolean disabled;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModified;
}
