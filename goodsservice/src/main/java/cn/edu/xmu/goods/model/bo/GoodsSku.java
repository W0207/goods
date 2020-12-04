package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.SkuCreatVo;
import cn.edu.xmu.goods.model.vo.SkuInputVo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GoodsSku implements VoObject, Serializable {

    private Long id;

    private Long goodsSpuId;

    private String skuSn;

    private String name;

    private Long originalPrice;

    private String configuration;

    private Long weight;

    private String imageUrl;

    private Integer inventory;

    private String detail;

    private Boolean disabled;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public GoodsSku(GoodsSkuPo goodsSkuPo, GoodsSpuPo goodsSpuPo) {
        this.id = goodsSkuPo.getId();
        this.goodsSpuId = goodsSpuPo.getId();
        this.skuSn = goodsSkuPo.getSkuSn();
        this.name = goodsSkuPo.getName();
        this.originalPrice = goodsSkuPo.getOriginalPrice();
        this.weight = goodsSkuPo.getWeight();
        this.imageUrl = goodsSkuPo.getImageUrl();
        this.inventory = goodsSkuPo.getInventory();
        this.detail = goodsSkuPo.getDetail();
        this.disabled = goodsSkuPo.getDisabled() == 1;
        this.gmtCreate = goodsSkuPo.getGmtCreate();
        this.gmtModified = goodsSkuPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        return new SkuRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    /**
     * 构造函数
     *
     * @param po Po对象
     */
    public GoodsSku(GoodsSkuPo po) {
        this.id = po.getId();
        this.goodsSpuId = po.getGoodsSpuId();
        this.skuSn = po.getSkuSn();
        this.name = po.getName();
        this.originalPrice = po.getOriginalPrice();
        this.configuration = po.getConfiguration();
        this.weight = po.getWeight();
        this.imageUrl = po.getImageUrl();
        this.inventory = po.getInventory();
        this.detail = po.getDetail();
        this.disabled = po.getDisabled() == 1;
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    /**
     * 创建删除sku的po
     *
     * @return
     */
    public GoodsSkuPo createDeleteStatePo() {
        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();
        goodsSkuPo.setId(id);
        goodsSkuPo.setDisabled((byte) 3);
        goodsSkuPo.setGmtModified(LocalDateTime.now());
        return goodsSkuPo;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public Long getId() {
        return id;
    }

    public Long getGoodsSpuId() {
        return goodsSpuId;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public Integer getInventory() {
        return inventory;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Long getWeight() {
        return weight;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getSkuSn() {
        return skuSn;
    }

    /**
     * 由vo创建po
     *
     * @param skuInputVo
     * @return
     */
    public GoodsSkuPo createUpdatePo(SkuInputVo skuInputVo) {
        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();
        goodsSkuPo.setId(id);
        String nameEnc = goodsSkuPo == null ? null : skuInputVo.getName();
        Long originalPriceEnc = skuInputVo.getOriginalPrice() == null ? null : skuInputVo.getOriginalPrice();
        String configurationEnc = skuInputVo.getConfiguration() == null ? null : skuInputVo.getConfiguration();
        Long inventoryEnc = skuInputVo.getInventory() == null ? null : skuInputVo.getInventory();
        Long weightEnc = skuInputVo.getWeight() == null ? null : skuInputVo.getWeight();
        String detailEnc = skuInputVo.getDetail() == null ? null : skuInputVo.getDetail();
        goodsSkuPo.setOriginalPrice(originalPriceEnc);
        goodsSkuPo.setName(nameEnc);
        goodsSkuPo.setDetail(detailEnc);
        goodsSkuPo.setWeight(inventoryEnc);
        goodsSkuPo.setWeight(weightEnc);
        goodsSkuPo.setConfiguration(configurationEnc);
        return goodsSkuPo;
    }

    /**
     * 由creatVo创建po
     *
     * @param skuCreatVo
     * @return
     */
    public GoodsSkuPo createPo(SkuCreatVo skuCreatVo, Long spuId) {
        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();
        String nameEnc = skuCreatVo.getName() == null ? null : skuCreatVo.getName();
        String skusnEnc = skuCreatVo.getSn() == null ? null : skuCreatVo.getName();
        Long originalPriceEnc = skuCreatVo.getOriginalPrice() == null ? null : skuCreatVo.getOriginalPrice();
        String configurationEnc = skuCreatVo.getConfiguration() == null ? null : skuCreatVo.getConfiguration();
        Long inventoryEnc = skuCreatVo.getInventory() == null ? null : skuCreatVo.getInventory();
        String imageUrlEnc = skuCreatVo.getImageUrl() == null ? null : skuCreatVo.getImageUrl();
        Long weightEnc = skuCreatVo.getWeight() == null ? null : skuCreatVo.getWeight();
        String detailEnc = skuCreatVo.getDetail() == null ? null : skuCreatVo.getDetail();
        goodsSkuPo.setOriginalPrice(originalPriceEnc);
        goodsSkuPo.setName(nameEnc);
        goodsSkuPo.setDetail(detailEnc);
        goodsSkuPo.setWeight(inventoryEnc);
        goodsSkuPo.setImageUrl(imageUrlEnc);
        goodsSkuPo.setWeight(weightEnc);
        goodsSkuPo.setConfiguration(configurationEnc);
        goodsSkuPo.setGmtCreate(LocalDateTime.now());
        goodsSkuPo.setGmtModified(LocalDateTime.now());
        goodsSkuPo.setGoodsSpuId(spuId);
        goodsSkuPo.setSkuSn(skusnEnc);
        return goodsSkuPo;
    }

    public GoodsSku (){}
}
