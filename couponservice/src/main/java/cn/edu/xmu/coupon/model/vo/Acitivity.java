package cn.edu.xmu.coupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
@Data
public class Acitivity {

    @ApiModelProperty("优惠活动名称")
    private String activityName;

    @ApiModelProperty("优惠活动图片")
    private String image_url;

    @ApiModelProperty("优惠活动开始时间")
    private LocalDateTime begin_time;

    @ApiModelProperty("优惠活动结束时间")
    private LocalDateTime end_time;

    @ApiModelProperty("优惠活动开始领优惠券时间")
    private LocalDateTime coupon_time;

    @ApiModelProperty("优惠活动优惠券数目")
    private Integer quantity;

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Acitivity(Acitivity acitivity) {
        this.activityName= acitivity.getActivityName();
        this.begin_time= acitivity.getBegin_time();
        this.coupon_time= acitivity.getCoupon_time();
        this.end_time= acitivity.getEnd_time();
        this.image_url= acitivity.getImage_url();
        this.quantity= acitivity.getQuantity();
    }

    public Acitivity() {

    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public void setCoupon_time(LocalDateTime coupon_time) {
        this.coupon_time = coupon_time;
    }

    public void setBegin_time(LocalDateTime begin_time) {
        this.begin_time = begin_time;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public LocalDateTime getCoupon_time() {
        return coupon_time;
    }

    public LocalDateTime getBegin_time() {
        return begin_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getActivityName() {
        return activityName;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
