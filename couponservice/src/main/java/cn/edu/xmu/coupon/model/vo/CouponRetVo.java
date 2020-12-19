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
public class CouponRetVo implements VoObject{


    @ApiModelProperty("优惠券ID")
    private Long id;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券编号")
    private String couponSn;

    private Acitivity acitivity;


    public CouponRetVo(CouponRet couponRet) {

        this.name=couponRet.getName();
        this.acitivity=couponRet.getAcitivity();
        this.id=couponRet.getId();
        this.couponSn=couponRet.getCouponSn();

    }

    public CouponRetVo() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }


    public Long getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public Acitivity getAcitivity() {
        return acitivity;
    }

    @Override
    public Object createVo() {
        CouponRetVo couponRetVo =new CouponRetVo();
        couponRetVo.acitivity=this.acitivity;
        couponRetVo.couponSn=this.couponSn;
        couponRetVo.id=this.id;
        couponRetVo.name=this.name;
        return couponRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
