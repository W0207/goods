package cn.edu.xmu.ininterface.service.model.vo;

import cn.edu.xmu.ooad.model.VoObject;
import com.sun.jdi.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Abin
 */
@Data
@AllArgsConstructor
public class SkuToFlashSaleVo implements VoObject, Serializable {

    private Long id;

    private String name;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private Boolean disable = false;

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
