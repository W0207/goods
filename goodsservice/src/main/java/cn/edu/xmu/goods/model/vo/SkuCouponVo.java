package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
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
}
