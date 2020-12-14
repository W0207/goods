package cn.edu.xmu.otherinterface.bo;

import java.io.Serializable;


public class GoodsSkuInfo implements Serializable {

    private Long id;

    private String skuName;

    private String spuName;

    private String skuSn;

    private String imgUrl;

    private int inventory;

    private Long originalPrice;

    private Long price;

    private boolean disable;

    public GoodsSkuInfo(Long id, String skuName, String spuName, String skuSn, String imgUrl, int inventory, Long originalPrice, Long price, boolean disable) {
        this.id = id;
        this.skuName = skuName;
        this.spuName = spuName;
        this.skuSn = skuSn;
        this.imgUrl = imgUrl;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
