package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "可修改的sku信息")
public class SkuInputVo {

    @ApiModelProperty(value = "商品型号名称")
    private String name;

    @ApiModelProperty(value = "该型号原价")
    private Long originalPrice;

    @ApiModelProperty("配置参数JSON")
    private String configuration;

    @ApiModelProperty("重量")
    private Long weight;

    @ApiModelProperty("库存")
    private Long inventory;

    @ApiModelProperty("该型号描述")
    private String detail;
}
