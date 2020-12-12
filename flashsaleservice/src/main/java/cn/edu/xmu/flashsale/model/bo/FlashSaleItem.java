package cn.edu.xmu.flashsale.model.bo;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.ooad.util.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.flashsale.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Data
public class FlashSaleItem implements VoObject{


    Long id;

    Long saleId;

    Long goodsSkuId;

    Long price;

    Integer quantity;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    public  FlashSaleItem(Long id,SkuInputVo skuInputVo){
        this.saleId=id;
        this.goodsSkuId=skuInputVo.getSkuId()==null ? null :skuInputVo.getSkuId();
        this.price=skuInputVo.getPrice()==null ? null : skuInputVo.getPrice();
        this.quantity=skuInputVo.getQuantity() == null ? null : skuInputVo.getQuantity();
        this.gmtCreate=LocalDateTime.now();
        this.gmtModified=LocalDateTime.now();

    }

    public FlashSaleItem(FlashSaleItemPo po){
        this.id=po.getId();
        this.saleId=po.getSaleId();
        this.goodsSkuId=po.getGoodsSkuId();
        this.price=po.getPrice();
        this.quantity=po.getQuantity();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }
    public FlashSaleItemPo createItemPo(){
        FlashSaleItemPo po =new FlashSaleItemPo();
        po.setSaleId(this.saleId);
        po.setGoodsSkuId(this.goodsSkuId);
        po.setPrice(this.price);
        po.setQuantity(this.quantity);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return  po;
    }

    public FlashSaleItem(){}
    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
