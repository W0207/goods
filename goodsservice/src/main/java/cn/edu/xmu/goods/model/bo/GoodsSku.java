package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.SkuInputVo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shibin zhan
 */
@Data
public class GoodsSku implements VoObject, Serializable {

    /**
     * 商品状态
     */
    public enum State {
        UNPUBLISHED(0, "未上架"),
        PUBLISHED(4, "上架"),
        DELETED(6, "已删除");

        private static final Map<Integer, GoodsSku.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static GoodsSku.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    private Long id;

    private Long goodsSpuId;

    private String skuSn;

    private String name;

    private Long originalPrice;

    private String configuration;

    private Long weight;

    private String imageUrl;

    private Integer inventory;

    private State state = GoodsSku.State.UNPUBLISHED;

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
}
