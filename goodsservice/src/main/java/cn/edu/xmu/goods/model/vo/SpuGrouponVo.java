package cn.edu.xmu.goods.model.vo;


import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SpuGrouponVo implements Serializable {
    private Long id;

    private String name;

    private String goodsSn;

    private String imageUrl;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Boolean disable =false;

    public SpuGrouponVo(GoodsSpuPo goodsSpuPo)
    {
        this.id=goodsSpuPo.getId();
        this.name=goodsSpuPo.getName();
        this.goodsSn=goodsSpuPo.getGoodsSn();
        this.imageUrl=goodsSpuPo.getImageUrl();
        this.gmtCreate=goodsSpuPo.getGmtCreate();
        this.gmtModified=goodsSpuPo.getGmtModified();

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
        return goodsSn;
    }

    public void setSkuSn(String skuSn) {
        this.goodsSn = skuSn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

