package cn.edu.xmu.coupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 菜鸡骞
 */
@Data
@ApiOperation(value = "优惠券信息")
public class CouponVo {

    @ApiModelProperty("优惠券Id")
    private Long id;

    @ApiModelProperty("顾客Id")
    private Long customerId;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券编号")
    private String couponSn;

    @ApiModelProperty("优惠券编号")
    private Integer state;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModified;


}
