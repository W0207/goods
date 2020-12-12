package cn.edu.xmu.flashsale.model.vo;
import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;

import java.time.LocalDateTime;

public class FlashSaleOutputVo {

    Long id;

    SkuToFlashSaleVo goodsSku;

    Long price;

    Integer quantity;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    public FlashSaleOutputVo (FlashSaleItem flashSaleItem, SkuToFlashSaleVo skuToFlashSaleVo){
        this.id=flashSaleItem.getId()==null ? null :flashSaleItem.getId();
        this.goodsSku=skuToFlashSaleVo==null ? null :skuToFlashSaleVo;
        this.price=flashSaleItem.getPrice()==null ? null : flashSaleItem.getPrice();
        this.gmtCreate=flashSaleItem.getGmtCreate()==null ? null :flashSaleItem.getGmtCreate();
        this.gmtModified=flashSaleItem.getGmtModified()==null ? null : flashSaleItem.getGmtModified();
    }
    public Long getId(){return this.id;}

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public SkuToFlashSaleVo getGoodsSku() {
        return goodsSku;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public Long getPrice() {
        return price;
    }
}
