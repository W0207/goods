package cn.edu.xmu.flashsale.model.bo;

import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.flashsale.model.vo.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class FlashSaleItem implements VoObject, Serializable {

    private Long id;

    private Long saleId;

    private Long goodsSkuId;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Product product;

    public FlashSaleItem() {
        super();
    }

    public FlashSaleItem(Long id, SkuInputVo skuInputVo) {
        this.saleId = id;
        this.goodsSkuId = skuInputVo.getSkuId() == null ? null : skuInputVo.getSkuId();
        this.price = skuInputVo.getPrice() == null ? null : skuInputVo.getPrice();
        this.quantity = skuInputVo.getQuantity() == null ? null : skuInputVo.getQuantity();
        this.gmtCreate = LocalDateTime.now();
        this.gmtModified = LocalDateTime.now();
    }

    public FlashSaleItem(FlashSaleItemPo itemPo, SkuToFlashSaleVo skuPo) {
        this.id = itemPo.getId();
        this.goodsSkuId = itemPo.getGoodsSkuId();
        this.product = new Product(skuPo);
        this.price = itemPo.getPrice();
        this.saleId = itemPo.getSaleId();
        this.quantity = itemPo.getQuantity();
        this.gmtCreate = itemPo.getGmtCreate();
        this.gmtModified = itemPo.getGmtModified();
    }

    public FlashSaleItem(FlashSaleItemPo po) {
        this.id = po.getId();
        this.saleId = po.getSaleId();
        this.goodsSkuId = po.getGoodsSkuId();
        this.price = po.getPrice();
        this.quantity = po.getQuantity();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public FlashSaleItemPo createItemPo() {
        FlashSaleItemPo po = new FlashSaleItemPo();
        po.setSaleId(this.saleId);
        po.setGoodsSkuId(this.goodsSkuId);
        po.setPrice(this.price);
        po.setQuantity(this.quantity);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }

    @Override
    public Object createVo() {
        FlashSaleItemRetVo retVo = new FlashSaleItemRetVo();
        retVo.setId(this.id);

        ProductRetVo productRetVo = (ProductRetVo) product.createVo();
        productRetVo.setPrice(this.price);
        productRetVo.setInventory(this.quantity);
        retVo.setGoodsSku(productRetVo);
        retVo.setPrice(this.price);
        retVo.setQuantity(this.quantity);
        retVo.setGmtCreate(this.gmtCreate);
        retVo.setGmtModified(this.gmtModified);
        return retVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
