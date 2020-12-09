package cn.edu.xmu.presale.model.vo;

import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleActivityVo {


    private String name;

    private Long advancePayPrice;

    private Long restPayPrice;

    private Integer quantity;

    private LocalDateTime beginTime;

    private LocalDateTime payTime;

    private LocalDateTime endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAdvancePayPrice() {
        return advancePayPrice;
    }

    public void setAdvancePayPrice(Long advancePayPrice) {
        this.advancePayPrice = advancePayPrice;
    }

    public Long getRestPayPrice() {
        return restPayPrice;
    }

    public void setRestPayPrice(Long restPayPrice) {
        this.restPayPrice = restPayPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public PresaleActivityPo creatPo()
    {
        PresaleActivityPo presaleActivityPo = new PresaleActivityPo();
        presaleActivityPo.setName(this.getName());
        presaleActivityPo.setAdvancePayPrice(this.advancePayPrice);
        presaleActivityPo.setRestPayPrice(this.getRestPayPrice());
        presaleActivityPo.setQuantity(this.getQuantity());
        presaleActivityPo.setBeginTime(this.getBeginTime());
        presaleActivityPo.setEndTime(this.getEndTime());
        presaleActivityPo.setPayTime(this.getPayTime());
        return presaleActivityPo;
    }
}
