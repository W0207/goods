package cn.edu.xmu.flashsale.model.bo;

import cn.edu.xmu.flashsale.model.vo.ProductRetVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Abin
 */
@Data
public class Product implements VoObject, Serializable {

    private Long id;

    private Boolean disabled;

    private String imgUrl;

    private Integer inventory;

    private String name;

    private Long originalPrice;

    private String skuSn;

    public Product(SkuToFlashSaleVo skuToFlashSaleVo) {
        this.id = skuToFlashSaleVo.getId();
        this.disabled = skuToFlashSaleVo.getDisable();
        this.imgUrl = skuToFlashSaleVo.getImageUrl();
        this.inventory = skuToFlashSaleVo.getInventory();
        this.name = skuToFlashSaleVo.getName();
        this.originalPrice = skuToFlashSaleVo.getOriginalPrice();
        this.skuSn = skuToFlashSaleVo.getSkuSn();
    }

    @Override
    public Object createVo() {
        ProductRetVo retVo = new ProductRetVo();
        retVo.setId(this.id);
        retVo.setDisable(this.disabled);
        retVo.setImageUrl(this.imgUrl);
        retVo.setInventory(this.inventory);
        retVo.setName(this.name);
        retVo.setOriginalPrice(this.originalPrice);
        retVo.setSkuSn(this.skuSn);
        return retVo;
    }

    public Product() {
        super();
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
