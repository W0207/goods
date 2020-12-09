package cn.edu.xmu.coupon.model.vo;

import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddCouponActivityVo {

    private String name;

    private Integer quantity;

    private Byte quantityType;

    private Byte validTerm;

    private LocalDateTime couponTime;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String strategy;

    public CouponActivityPo createPo(){
        CouponActivityPo po = new CouponActivityPo();
        po.setName(this.name);
        po.setQuantity(this.quantity);
        po.setQuantitiyType(this.quantityType);
        po.setValidTerm(this.validTerm);
        po.setCouponTime(this.couponTime);
        po.setBeginTime(this.beginTime);
        po.setEndTime(this.beginTime);
        po.setStrategy(this.strategy);
        return po;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Byte getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Byte quantityType) {
        this.quantityType = quantityType;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Byte getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Byte validTerm) {
        this.validTerm = validTerm;
    }

    public LocalDateTime getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(LocalDateTime couponTime) {
        this.couponTime = couponTime;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }



    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
