package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleCouponActivityVo {

    @ApiModelProperty("活动Id")
    private Long id;

    @ApiModelProperty("优惠活动名称")
    private String name;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("优惠券数目")
    private Integer quantity;

    @ApiModelProperty("开始领券时间")
    private LocalDateTime couponTime;

}
