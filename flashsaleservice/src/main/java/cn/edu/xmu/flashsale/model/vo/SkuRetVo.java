package cn.edu.xmu.flashsale.model.vo;

import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import lombok.Data;

/**
 * @author Abin
 */
@Data
public class SkuRetVo {
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Boolean disable;

    public SkuRetVo() {

    }

    public SkuRetVo(SkuToFlashSaleVo skuToFlashSaleVo) {
        this.id = skuToFlashSaleVo.getId();
        this.name = skuToFlashSaleVo.getName();
        this.skuSn = skuToFlashSaleVo.getSkuSn();
        this.imageUrl = skuToFlashSaleVo.getImageUrl();
        this.inventory = skuToFlashSaleVo.getInventory();
        this.originalPrice = skuToFlashSaleVo.getOriginalPrice();
        this.price = skuToFlashSaleVo.getPrice();
        this.disable = skuToFlashSaleVo.getDisable();
    }

}
