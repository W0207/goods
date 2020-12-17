package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.vo.CouponRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
public class CouponRet implements VoObject {

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


    public CouponRet(Coupon co) {
        this.id=co.getId();
        this.activityId=co.getActivityId();
        this.name= co.getName();
        this.couponSn=co.getCouponSn();
    }

    public CouponRet(CouponRetVo couponRetVo) {
        this.activityId=couponRetVo.getActivityId();
        this.activityName=couponRetVo.getActivityName();
        this.id=couponRetVo.getId();
        this.name=couponRetVo.getName();
        this.couponSn=couponRetVo.getCouponSn();
        this.begin_time=couponRetVo.getBegin_time();
        this.end_time=couponRetVo.getEnd_time();
        this.image_url=couponRetVo.getImage_url();
        this.coupon_time=couponRetVo.getCoupon_time();
        this.quantity=couponRetVo.getQuantity();
    }

    public void SetByActivity(CouponActivityPo couponActivityPo)
    {
        this.activityName=couponActivityPo.getName();
        this.begin_time=couponActivityPo.getBeginTime();
        this.end_time=couponActivityPo.getEndTime();
        this.image_url=couponActivityPo.getImageUrl();
        this.coupon_time=couponActivityPo.getCouponTime();
        this.quantity=couponActivityPo.getQuantity();
    }

    @Override
    public Object createVo() {

        return new CouponRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
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

    public LocalDateTime getCoupon_time() {
        return coupon_time;
    }

    public String getActivityName() {
        return activityName;
    }


}
