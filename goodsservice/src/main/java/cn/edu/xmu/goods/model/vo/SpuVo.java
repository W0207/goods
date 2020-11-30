package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpuVo {

    private Long id;

    @ApiModelProperty("品牌信息")
    private SimpleBrandVo Brand;

    @ApiModelProperty("分类信息")
    private SimpleCategoryVo category;

//    @ApiModelProperty("运费模板信息")
//    private FreightModel freightModel;

    @ApiModelProperty("商店信息")
    private SimpleShopVo shop;

    private String goodsSn;

    private String detail;

    @ApiModelProperty("商品规格")
    private SpecVo spec;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Boolean disable;
}
