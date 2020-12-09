package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SkuPresaleVo implements Serializable {
    private Long id;

    private String name;

    private String skuSn;

    private String imageUrl;

    private Byte state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Boolean disable =false;

    public SkuPresaleVo(GoodsSkuPo goodsSkuPo)
    {
        this.id=goodsSkuPo.getId();
        this.name=goodsSkuPo.getName();
        this.skuSn=goodsSkuPo.getSkuSn();
        this.imageUrl=goodsSkuPo.getImageUrl();
        this.state=goodsSkuPo.getState();
        this.gmtCreate=goodsSkuPo.getGmtCreate();
        this.gmtModified=goodsSkuPo.getGmtModified();

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

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
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

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
