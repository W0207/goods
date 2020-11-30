package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.ooad.model.VoObject;

import java.time.LocalDateTime;

public class FlashSaleItem implements VoObject {

    private Long id;

    private Long saleId;

    private Long goodsSkuId;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public FlashSaleItem(FlashSaleItemPo flashSaleItemPo, GoodsSkuPo goodsSkuPo) {
        this.id = flashSaleItemPo.getId();
        this.saleId = flashSaleItemPo.getSaleId();
        this.goodsSkuId = goodsSkuPo.getId();
        this.price = flashSaleItemPo.getPrice();
        this.quantity = flashSaleItemPo.getQuantity();
        this.gmtCreate = flashSaleItemPo.getGmtCreate();
        this.gmtModified = flashSaleItemPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
