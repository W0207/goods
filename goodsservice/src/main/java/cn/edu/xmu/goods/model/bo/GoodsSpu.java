package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @author shibin zhan
 */
@Data
public class GoodsSpu implements VoObject {

    private Long id;

    private String name;

    private Long brandId;

    private Long categoryId;

    private Long freightId;

    private Long shopId;

    private String goodsSn;

    private String detail;

    private String imageUrl;

    private String spec;

    private Boolean disabled;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    /**
     * 构造函数
     *
     * @param goodsSpuPo Po对象
     */
    public GoodsSpu(GoodsSpuPo goodsSpuPo) {
        this.id = goodsSpuPo.getId();
        this.name = goodsSpuPo.getName();
        this.brandId = goodsSpuPo.getBrandId();
        this.categoryId = goodsSpuPo.getCategoryId();
        this.freightId = goodsSpuPo.getFreightId();
        this.shopId = goodsSpuPo.getShopId();
        this.goodsSn = goodsSpuPo.getGoodsSn();
        this.detail = goodsSpuPo.getDetail();
        this.imageUrl = goodsSpuPo.getImageUrl();
        this.spec = goodsSpuPo.getSpec();
        this.disabled = goodsSpuPo.getDisabled() == 0 ? true : false;
        this.gmtCreated = goodsSpuPo.getGmtCreate();
        this.gmtModified = goodsSpuPo.getGmtModified();
    }

    /**
     * 用 vo 对象创建用来更新 spu 的 po 对象
     *
     * @param spuInputVo vo 对象
     * @return po 对象
     */
    public GoodsSpuPo createUpdatePo(SpuInputVo spuInputVo) {
        String nameEnc = spuInputVo.getName() == null ? null : spuInputVo.getName();
        String descriptionEnc = spuInputVo.getDescription() == null ? null : spuInputVo.getDescription();
        String specsEnc = spuInputVo.getSpecs() == null ? null : spuInputVo.getSpecs();

        GoodsSpuPo goodsSpuPo = new GoodsSpuPo();

        goodsSpuPo.setId(id);
        goodsSpuPo.setName(nameEnc);
        goodsSpuPo.setDetail(descriptionEnc);
        goodsSpuPo.setSpec(specsEnc);
        goodsSpuPo.setGmtModified(LocalDateTime.now());
        return goodsSpuPo;
    }

    /**
     * 修改商品的上下架状态
     *
     * @param code
     * @return po对象
     */
    public GoodsSpuPo createUpdateStatePo(Long code) {
        GoodsSpuPo goodsSpuPo = new GoodsSpuPo();
        goodsSpuPo.setId(id);
        goodsSpuPo.setGmtModified(LocalDateTime.now());
        return goodsSpuPo;
    }

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }


}