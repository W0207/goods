package cn.edu.xmu.goods.model.bo;


/**
 * @author BiuBiuBiu
 */
public class FindSkuRet {

    private Long id;

    private String name;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private Boolean disable;

    public FindSkuRet(GoodsSku sku) {
        this.skuSn = sku.getSkuSn();
        this.disable = sku.getDisabled();
        this.id = sku.getId();
        this.imageUrl = sku.getImageUrl();
        this.inventory = sku.getInventory();
        this.name = sku.getName();
        this.originalPrice = sku.getOriginalPrice();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Integer getInventory() {
        return inventory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public String getSkuSn() {
        return skuSn;
    }

    public Boolean getDisable() {
        return disable;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        if(price==null)
        {
            this.price=originalPrice;
        }
        else
        {
            this.price=price;
        }
    }
}
