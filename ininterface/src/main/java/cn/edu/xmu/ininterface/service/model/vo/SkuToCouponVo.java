package cn.edu.xmu.ininterface.service.model.vo;

import java.io.Serializable;

/**
 * @author BiuBiuBiu
 */
public class SkuToCouponVo implements Serializable {

    private Long id;

    private String name;

    private String goodsSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Byte disable;

    public SkuToCouponVo(SkuToCouponVo vo) {
        this.id = vo.getId();
        this.disable = vo.getDisable();
        this.goodsSn = vo.getGoodsSn();
        this.imageUrl = vo.getImageUrl();
        this.inventory = vo.getInventory();
        this.name = vo.getName();
        this.originalPrice = vo.originalPrice;
    }

    public SkuToCouponVo() {

    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setDisable(byte disable) {
        this.disable = disable;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Byte getDisable() {
        return disable;
    }

    public Integer getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
