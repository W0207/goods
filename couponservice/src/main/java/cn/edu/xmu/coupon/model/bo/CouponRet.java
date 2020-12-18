package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.vo.Acitivity;
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

    private Acitivity acitivity = new Acitivity();


    public CouponRet(Coupon co) {
        this.id=co.getId();
        this.acitivity.setId(co.getActivityId());
        this.name= co.getName();
        this.couponSn=co.getCouponSn();
    }


    public void SetByActivity(CouponActivityPo couponActivityPo)
    {
        this.acitivity.setActivityName(couponActivityPo.getName());
        this.acitivity.setBegin_time(couponActivityPo.getBeginTime());
        this.acitivity.setQuantity(couponActivityPo.getQuantity());
        this.acitivity.setEnd_time(couponActivityPo.getEndTime());
        this.acitivity.setImage_url(couponActivityPo.getImageUrl());
        this.acitivity.setCoupon_time(couponActivityPo.getCouponTime());
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


    public Long getId() {
        return id;
    }

    public Acitivity getAcitivity() {
        return acitivity;
    }
}
