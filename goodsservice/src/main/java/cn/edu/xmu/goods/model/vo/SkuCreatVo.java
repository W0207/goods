package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "新建需要的sku信息")
public class SkuCreatVo {

    @ApiModelProperty(value = "关联商品spu")
    private String sn;

    @ApiModelProperty(value = "商品型号名称")
    private String name;

    @ApiModelProperty(value = "该型号原价")
    private Long originalPrice;

    @ApiModelProperty("配置参数JSON")
    private String configuration;

    @ApiModelProperty("重量")
    private Long weight;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("库存")
    private Integer inventory;

    @ApiModelProperty("该型号描述")
    private String detail;
}
