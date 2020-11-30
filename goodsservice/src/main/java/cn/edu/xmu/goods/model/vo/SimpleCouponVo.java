package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SimpleCouponVo {

    @ApiModelProperty("优惠券id")
    private Long id;

    @ApiModelProperty("优惠券适用的优惠活动列表")
    List<SimpleCouponActivityVo> simpleCouponActivityVoList;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券编号")
    private String couponSn;
}
