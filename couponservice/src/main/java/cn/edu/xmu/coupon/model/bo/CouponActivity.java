package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.vo.CouponActivityModifyVo;
import cn.edu.xmu.coupon.model.vo.CouponActivityRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 菜鸡骞
 */
@Data
public class CouponActivity implements VoObject {

    private Long id;

    private String name;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private LocalDateTime begin_time;

    private LocalDateTime end_time;

    private LocalDateTime coupon_time;

    private int state;

    private Long shop_id;

    private int quantity;

    private int valid_term;

    private String image_url;

    private String strategy;

    private Long created_by;

    private Long modi_by;

    private int quantity_type;

    public CouponActivity(CouponActivityPo po) {
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

    public Long getId() {
        return id;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public int getState() {
        return state;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantity_type() {
        return quantity_type;
    }

    public int getValid_term() {
        return valid_term;
    }

    public LocalDateTime getBegin_time() {
        return begin_time;
    }

    public LocalDateTime getCoupon_time() {
        return coupon_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public Long getCreated_by() {
        return created_by;
    }

    public Long getModi_by() {
        return modi_by;
    }

    public Long getShop_id() {
        return shop_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStrategy() {
        return strategy;
    }

    @Override
    public Object createVo() {
        return new CouponActivityRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public CouponActivityPo createModifyPo(CouponActivityModifyVo couponActivityModifyVo) {
        CouponActivityPo couponActivityPo=new CouponActivityPo();
        couponActivityPo.setName(couponActivityModifyVo.getName());
        couponActivityPo.setBeginTime(couponActivityModifyVo.getBegin_time());
        couponActivityPo.setEndTime(couponActivityModifyVo.getEnd_time());
        couponActivityPo.setQuantity(couponActivityModifyVo.getQuantity());
        couponActivityPo.setStrategy(couponActivityModifyVo.getStrategy());
        couponActivityPo.setId(id);
        return couponActivityPo;
    }
}
