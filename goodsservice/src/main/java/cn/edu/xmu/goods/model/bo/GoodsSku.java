package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.SkuInputVo;
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
        return null;
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

    public GoodsSkuPo createDeleteStatePo() {
        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();
        goodsSkuPo.setId(id);
        goodsSkuPo.setDisabled((byte) 3);
        goodsSkuPo.setGmtModified(LocalDateTime.now());
        return goodsSkuPo;
    }

    /**
     * vo创建po
     *
     * @param skuInputVo
     * @return GoodsSkuPo
     */
    public GoodsSkuPo createUpdatePo(SkuInputVo skuInputVo) {
        String nameEnc = skuInputVo.getName() == null ? null : skuInputVo.getName();
        Long originalPriceEnc = skuInputVo.getOriginalPrice() == null ? null : skuInputVo.getOriginalPrice();
        String configurationEnc = skuInputVo.getConfiguration() == null ? null : skuInputVo.getConfiguration();
        Long weightEnc = skuInputVo.getWeight() == null ? null : skuInputVo.getWeight();
        Integer inventoryEnc = skuInputVo.getInventory() == null ? null : skuInputVo.getInventory();
        String detailEnc = skuInputVo.getDetail() == null ? null : skuInputVo.getDetail();

        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();

        goodsSkuPo.setId(id);
        goodsSkuPo.setName(nameEnc);
        goodsSkuPo.setOriginalPrice(originalPriceEnc);
        goodsSkuPo.setConfiguration(configurationEnc);
        goodsSkuPo.setWeight(weightEnc);
        goodsSkuPo.setInventory(inventoryEnc);
        goodsSkuPo.setDetail(detailEnc);
        goodsSkuPo.setGmtModified(LocalDateTime.now());

        return goodsSkuPo;
    }
}
