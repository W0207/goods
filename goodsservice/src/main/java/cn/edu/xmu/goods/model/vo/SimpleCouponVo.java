package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SimpleCouponVo {

    private Long id;

    @ApiModelProperty("优惠券适用的优惠活动列表")
    List<SimpleCouponActivityVo> activityVoList;

    private String name;

    private String couponSn;
}
