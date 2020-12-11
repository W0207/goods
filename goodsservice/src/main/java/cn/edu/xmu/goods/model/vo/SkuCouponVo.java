package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import lombok.Data;

import java.io.Serializable;

@Data
public class SkuCouponVo implements Serializable {

    private Long id;

    private String name;

    private String goodsSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Byte disable;

    public SkuCouponVo(GoodsSkuPo goodsSkuPo) {
        this.id=goodsSkuPo.getId();
        this.disable=goodsSkuPo.getDisabled();
        this.goodsSn=goodsSkuPo.getSkuSn();
        this.imageUrl=goodsSkuPo.getImageUrl();
        this.inventory=goodsSkuPo.getInventory();
        this.name=goodsSkuPo.getName();
        this.originalPrice=goodsSkuPo.getOriginalPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Integer getInventory() {
        return inventory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Byte getDisable() {
        return disable;
    }

    public String getGoodsSn() {
        return goodsSn;
    }
}
