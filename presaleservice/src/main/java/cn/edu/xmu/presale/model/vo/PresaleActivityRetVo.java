package cn.edu.xmu.presale.model.vo;

import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleActivityRetVo implements VoObject {
    private Long id;

    private String name;

    private Long advancePayPrice;

    private Long restPayPrice;

    private Integer quantity;

    private LocalDateTime beginTime;

    private LocalDateTime payTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Byte state;

    private ShopToAllVo shop;

    private SkuToPresaleVo goodsSku;

    public PresaleActivityRetVo(PresaleActivityPo presaleActivityPo)
    {
        this.id=presaleActivityPo.getId();
        this.name=presaleActivityPo.getName();
        this.advancePayPrice =presaleActivityPo.getAdvancePayPrice();
        this.restPayPrice = presaleActivityPo.getRestPayPrice();
        this.quantity = presaleActivityPo.getQuantity();
        this.beginTime = presaleActivityPo.getBeginTime();
        this.payTime = presaleActivityPo.getPayTime();
        this.endTime = presaleActivityPo.getEndTime();
        this.gmtCreate = presaleActivityPo.getGmtCreate();
        this.gmtModified = presaleActivityPo.getGmtModified();
        this.state = presaleActivityPo.getState();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShopToAllVo getShop() {
        return shop;
    }

    public void setShop(ShopToAllVo shop) {
        this.shop = shop;
    }

    public SkuToPresaleVo getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(SkuToPresaleVo goodsSku) {
        this.goodsSku = goodsSku;
    }

    public PresaleActivityRetVo(PresaleActivityRetVo vo) {
        this.endTime= vo.endTime;
        this.state = vo.state;
        this.gmtModified = vo.gmtModified;
        this.gmtCreate = vo.gmtCreate;
        this.payTime = vo.payTime;
        this.beginTime = vo.beginTime;
        this.quantity = vo.quantity;
        this.restPayPrice = vo.restPayPrice;
        this.advancePayPrice = vo.advancePayPrice;
        this.name = vo.name;
        this.id = vo.id;
        this.goodsSku = vo.goodsSku;
        this.shop = vo.shop;
    }

    @Override
    public Object createVo() {
        return new PresaleActivityRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
