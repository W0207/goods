package cn.edu.xmu.coupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiOperation(value = "可修改的优惠活动信息")
public class CouponActivityModifyVo {

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "优惠券数目")
    private Integer quantity;

    @ApiModelProperty(value = "活动开始时间")
    private LocalDateTime begin_time;

    @ApiModelProperty(value = "活动结束时间")
    private LocalDateTime end_time;

    @ApiModelProperty(value = "活动规则")
    private String strategy;

    public String getStrategy() {
        return strategy;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public LocalDateTime getBegin_time() {
        return begin_time;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
