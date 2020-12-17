package cn.edu.xmu.coupon.model.vo;

import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import io.lettuce.core.StrAlgoArgs;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 菜鸡骞
 */
@Data
public class CouponActivityRetVo {

    private Long id;

    private String name;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private LocalDateTime begin_time;

    private LocalDateTime end_time;

    private LocalDateTime coupon_time;

    private Byte state;

    private Long shop_id;

    private Integer quantity;

    private Byte valid_term;

    private String image_url;

    private String strategy;

    private Long created_by;

    private Long modi_by;

    private Byte quantity_type;

    public CouponActivityRetVo(CouponActivityPo po) {
        this.id=po.getId();
        this.name=po.getName();
        this.begin_time=po.getBeginTime();
        this.coupon_time=po.getCouponTime();
        this.created_by=po.getCreatedBy();
        this.end_time=po.getEndTime();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.image_url=po.getImageUrl();
        this.modi_by=po.getModiBy();
        this.quantity=po.getQuantity();
        this.quantity_type=po.getQuantitiyType();
        this.shop_id=po.getShopId();
        this.state=po.getState();
        this.strategy=po.getStrategy();
        this.valid_term=po.getValidTerm();
    }

    public CouponActivityRetVo(CouponActivity couponActivity)
    {
        this.id=couponActivity.getId();
        this.name=couponActivity.getName();
        this.begin_time=couponActivity.getBegin_time();
        this.coupon_time=couponActivity.getCoupon_time();
        this.created_by=couponActivity.getCreated_by();
        this.end_time=couponActivity.getEnd_time();
        this.gmtCreate=couponActivity.getGmtCreate();
        this.gmtModified=couponActivity.getGmtModified();
        this.image_url=couponActivity.getImage_url();
        this.modi_by=couponActivity.getModi_by();
        this.quantity=couponActivity.getQuantity();
        this.quantity_type=couponActivity.getQuantity_type();
        this.shop_id=couponActivity.getShop_id();
        this.state=couponActivity.getState();
        this.strategy=couponActivity.getStrategy();
        this.valid_term=couponActivity.getValid_term();
    }
}
