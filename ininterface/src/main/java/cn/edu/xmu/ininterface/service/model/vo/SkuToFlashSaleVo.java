package cn.edu.xmu.ininterface.service.model.vo;

import cn.edu.xmu.ooad.model.VoObject;
import com.sun.jdi.Value;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Abin
 */
@Data
public class SkuToFlashSaleVo implements VoObject, Serializable {

    private Long id;

    private String name;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private Boolean disable = false;

    public SkuToFlashSaleVo(Long id, String name, String skuSn, String imageUrl, Integer inventory, Long originalPrice, Long price, Boolean disable) {
        this.id = id;
        this.name = name;
        this.skuSn = skuSn;
        this.imageUrl = imageUrl;
        this.inventory = inventory;
        this.originalPrice = originalPrice;
        this.price = price;
        this.disable = disable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public SkuToFlashSaleVo() {

    }

    @Override
    public Object createVo() {
        SkuToFlashSaleVo skuToFlashSaleVo = new SkuToFlashSaleVo();
        skuToFlashSaleVo.setId(this.id);
        skuToFlashSaleVo.setDisable(this.disable);
        skuToFlashSaleVo.setSkuSn(this.skuSn);
        skuToFlashSaleVo.setName(this.name);
        skuToFlashSaleVo.setInventory(this.inventory);
        skuToFlashSaleVo.setOriginalPrice(this.originalPrice);
        skuToFlashSaleVo.setImageUrl(this.imageUrl);
        skuToFlashSaleVo.setPrice(this.price);
        return skuToFlashSaleVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
