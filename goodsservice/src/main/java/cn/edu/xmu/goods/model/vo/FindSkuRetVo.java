package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.FindSkuRet;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

/**
 * @author BiuBiuBiu
 */

@Data
public class FindSkuRetVo implements VoObject {

    private Long id;

    private String name;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private Boolean disable;

    public FindSkuRetVo(FindSkuRet skuRet) {
        this.id = skuRet.getId();
        this.name = skuRet.getName();
        this.skuSn = skuRet.getSkuSn();
        this.imageUrl = skuRet.getImageUrl();
        this.inventory = skuRet.getInventory();
        this.disable = skuRet.getDisable();
        this.originalPrice = skuRet.getOriginalPrice();
        this.price = skuRet.getPrice();
    }

    public FindSkuRetVo() {

    }

    @Override
    public Object createVo() {
        FindSkuRetVo vo = new FindSkuRetVo();
        vo.setDisable(this.disable);
        vo.setId(this.id);
        vo.setImageUrl(this.imageUrl);
        vo.setInventory(this.inventory);
        vo.setName(this.name);
        vo.setSkuSn(this.skuSn);
        vo.setOriginalPrice(this.originalPrice);
        vo.setPrice(this.price);
        return vo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }
}
