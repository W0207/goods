package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponVo {

    private Long id;

    @ApiModelProperty("优惠券适用的优惠活动列表")
    List<SimpleCouponActivityVo> simpleCouponActivityVoList;

    private Long customerId;

    private String name;

    private String couponSn;

    private Integer state;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
