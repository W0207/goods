package cn.edu.xmu.coupon.model.vo;

import cn.edu.xmu.coupon.model.bo.Coupon;
import cn.edu.xmu.coupon.model.bo.CouponRet;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.ooad.model.VoObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
public class CouponRetVo {


    @ApiModelProperty("优惠券ID")
    private Long id;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券编号")
    private String couponSn;

    @ApiModelProperty("优惠活动ID")
    private Long activityId;

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

    public CouponRetVo(CouponRet couponRet) {
        this.quantity=couponRet.getQuantity();
        this.coupon_time=couponRet.getCoupon_time();
        this.image_url=couponRet.getImage_url();
        this.end_time=couponRet.getEnd_time();
        this.name=couponRet.getName();
        this.activityId=couponRet.getActivityId();
        this.activityName=couponRet.getActivityName();
        this.begin_time=couponRet.getBegin_time();
        this.id=couponRet.getId();
        this.couponSn=couponRet.getCouponSn();

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setBegin_time(LocalDateTime begin_time) {
        this.begin_time = begin_time;
    }

    public void setCoupon_time(LocalDateTime coupon_time) {
        this.coupon_time = coupon_time;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getActivityName() {
        return activityName;
    }

    public String getImage_url() {
        return image_url;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public LocalDateTime getBegin_time() {
        return begin_time;
    }

    public Long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public String getName() {
        return name;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getCoupon_time() {
        return coupon_time;
    }
}
