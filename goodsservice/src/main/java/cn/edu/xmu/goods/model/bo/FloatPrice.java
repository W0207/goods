package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FloatPrice implements VoObject {

    private Long id;

    private Long goodsSkuId;

    private Long activityPrice;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer quantity;

    private Long createdBy;

    private Long invalidBy;

    private Boolean valid;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public FloatPrice(FloatPricePo floatPricePo) {
        this.id = floatPricePo.getId();
        this.goodsSkuId = floatPricePo.getGoodsSkuId();
        this.activityPrice = floatPricePo.getActivityPrice();
        this.beginTime = floatPricePo.getBeginTime();
        this.endTime = floatPricePo.getEndTime();
        this.quantity = floatPricePo.getQuantity();
        this.createdBy = floatPricePo.getCreatedBy();
        this.invalidBy = floatPricePo.getInvalidBy();
        this.valid = floatPricePo.getValid() == 1;
        this.gmtCreate = floatPricePo.getGmtCreate();
        this.gmtModified = floatPricePo.getGmtModified();
    }

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public FloatPricePo createUpdateStatePo(Long loginUserId) {
        FloatPricePo floatPricePo = new FloatPricePo();
        floatPricePo.setId(id);
        floatPricePo.setValid((byte) 1);//修改价格浮动为无效
        floatPricePo.setInvalidBy(loginUserId);//记录修改者的ID
        floatPricePo.setGmtModified(LocalDateTime.now());//记录修改的时间
        return floatPricePo;
    }
}
