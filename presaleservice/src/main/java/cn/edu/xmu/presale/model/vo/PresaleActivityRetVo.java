package cn.edu.xmu.presale.model.vo;

import cn.edu.xmu.ininterface.service.model.vo.ShopToPresaleVo;
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

    private ShopToPresaleVo shopToPresaleVo;

    private SkuToPresaleVo spuToPresaleVo;

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

    public ShopToPresaleVo getShopToPresaleVo() {
        return shopToPresaleVo;
    }

    public void setShopToPresaleVo(ShopToPresaleVo shopToPresaleVo) {
        this.shopToPresaleVo = shopToPresaleVo;
    }

    public SkuToPresaleVo getSpuToPresaleVo() {
        return spuToPresaleVo;
    }

    public void setSpuToPresaleVo(SkuToPresaleVo spuToPresaleVo) {
        this.spuToPresaleVo = spuToPresaleVo;
    }
}
