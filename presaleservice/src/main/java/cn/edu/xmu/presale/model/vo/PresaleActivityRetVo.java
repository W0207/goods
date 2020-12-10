package cn.edu.xmu.presale.model.vo;

import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;
import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleActivityRetVo {
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

    private ShopToAllVo shopToAllVo;

    private SkuToPresaleVo skuToPresaleVo;

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

    public ShopToAllVo getShopToAllVo() {
        return shopToAllVo;
    }

    public void setShopToAllVo(ShopToAllVo shopToAllVo) {
        this.shopToAllVo = shopToAllVo;
    }

    public SkuToPresaleVo getSkuToPresaleVo() {
        return skuToPresaleVo;
    }

    public void setSkuToPresaleVo(SkuToPresaleVo spuToPresaleVo) {
        this.skuToPresaleVo = spuToPresaleVo;
    }
}
