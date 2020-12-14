package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;

import java.time.LocalDateTime;

public class ShopRetVo {
    private Long id;

    private String name;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Byte state;

    public ShopRetVo(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.gmtCreate = shop.getGmtCreate();
        this.gmtModified = shop.getGmtModified();
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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}
