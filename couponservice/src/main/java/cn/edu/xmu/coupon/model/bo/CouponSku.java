package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.vo.CouponActivitySkuInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
@Data
public class CouponSku implements VoObject {

    private Long id;

    private Long activity_id;

    private Long sku_id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public CouponSku(CouponSkuPo couponSkuPo) {
        this.activity_id = couponSkuPo.getActivityId();
        this.id = couponSkuPo.getId();
        this.sku_id = couponSkuPo.getSkuId();
        this.gmtCreate = couponSkuPo.getGmtCreate();
        this.gmtModified = couponSkuPo.getGmtModified();

    }

    public CouponSku() {

    }

    public CouponSkuPo createAddCouponActivityPo(CouponActivitySkuInputVo couponActivitySkuInputVo, Long id, Long shopId) {
        CouponSkuPo couponSkuPo = new CouponSkuPo();
        couponSkuPo.setId(couponActivitySkuInputVo.getId());
        couponSkuPo.setGmtCreate(LocalDateTime.now());
        couponSkuPo.setActivityId(couponActivitySkuInputVo.getActivity_id());
        couponSkuPo.setSkuId(couponActivitySkuInputVo.getSku_id());
        return couponSkuPo;
    }

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
