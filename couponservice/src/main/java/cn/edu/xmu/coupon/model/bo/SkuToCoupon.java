package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.ininterface.service.model.vo.SkuToCouponVo;
import cn.edu.xmu.ooad.model.VoObject;

import java.io.Serializable;

/**
 * @author BiuBiuBiu
 */
public class SkuToCoupon implements VoObject {
    private Long id;

    private String name;

    private String goodsSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Byte disable;


    public SkuToCoupon(SkuToCouponVo vo) {
        this.id=vo.getId();
        this.goodsSn=vo.getGoodsSn();
        this.disable=vo.getDisable();
        this.imageUrl=vo.getImageUrl();
        this.inventory=vo.getInventory();
        this.name=vo.getName();
        this.originalPrice=vo.getOriginalPrice();
    }

    @Override
    public Object createVo() {
       SkuToCouponVo skuToCouponVo= new SkuToCouponVo();
       skuToCouponVo.setName(this.name);
       skuToCouponVo.setOriginalPrice(this.originalPrice);
       skuToCouponVo.setInventory(this.inventory);
       skuToCouponVo.setImageUrl(this.imageUrl);
       skuToCouponVo.setGoodsSn(this.goodsSn);
       skuToCouponVo.setDisable(this.disable);
       skuToCouponVo.setId(this.id);
       return skuToCouponVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
