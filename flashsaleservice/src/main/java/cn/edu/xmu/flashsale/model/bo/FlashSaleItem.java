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

    Long id;

    Long saleId;

    Long goodsSkuId;

    Long price;

    Integer quantity;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    SkuToFlashSaleVo goodsSku;

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
        this.goodsSku =skuPo;
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

    public FlashSaleItem() {
    }

    @Override
    public Object createVo() {
        FlashSaleItemRetVo flashSaleItemRetVo = new FlashSaleItemRetVo();

        flashSaleItemRetVo.setId(this.id);

        SkuToFlashSaleVo goodsSku=new SkuToFlashSaleVo();


        goodsSku.setPrice(this.price);
        goodsSku.setInventory(this.quantity);
        flashSaleItemRetVo.setGoodsSku(goodsSku);

        flashSaleItemRetVo.setPrice(this.price);
        flashSaleItemRetVo.setQuantity(this.quantity);
        flashSaleItemRetVo.setGmtCreate(this.gmtCreate);
        flashSaleItemRetVo.setGmtModified(this.gmtModified);

        return flashSaleItemRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
