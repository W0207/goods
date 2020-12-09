package cn.edu.xmu.coupon.model.vo;

import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ASUS
 */
@Data
public class AddCouponActivityRetVo {
    private Long id;

    private String name;

    private Byte state;

    private ShopToAllVo shop;

    private Integer quantity;

    private Byte quantityType;

    private Byte validTerm;

    private String imageUrl;

    private LocalDateTime couponTime;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String strategy;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public AddCouponActivityRetVo(CouponActivityPo po) {
        this.id = po.getId();
        this.name = po.getName();
        this.state = po.getState();
        this.quantity = po.getQuantity();
        this.quantityType = po.getQuantitiyType();
        this.validTerm = po.getValidTerm();
        this.imageUrl = po.getImageUrl();
        this.beginTime = po.getBeginTime();
        this.endTime = po.getEndTime();
        this.couponTime = po.getCouponTime();
        this.strategy = po.getStrategy();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public ShopToAllVo getShop() {
        return shop;
    }

    public void setShop(ShopToAllVo shop) {
        this.shop = shop;
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

    public Byte getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Byte validTerm) {
        this.validTerm = validTerm;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
