package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品sku详细信息
 */
@Data
public class SkuOutputVo {

    @ApiModelProperty("skuId")
    private Long id;

    @ApiModelProperty("sku名字")
    private String name;

    @ApiModelProperty("sku编号")
    private String skuSn;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("库存")
    private Integer inventory;

    @ApiModelProperty("原价")
    private Long originalPrice;

    @ApiModelProperty("现价")
    private Long price;

    @ApiModelProperty("是否被删除")
    private Boolean disabled;

    public SkuOutputVo(GoodsSkuPo goodsSkuPo) {
        Long skuId = goodsSkuPo.getId() == null ? null : goodsSkuPo.getId();
        String nameEnc = goodsSkuPo.getName() == null ? null : goodsSkuPo.getName();
        String skuSnEnc = goodsSkuPo.getSkuSn() == null ? null : goodsSkuPo.getSkuSn();
        String imageUrlEnc = goodsSkuPo.getImageUrl() == null ? null : goodsSkuPo.getImageUrl();
        Integer inventoryEnc = goodsSkuPo.getInventory() == null ? null : goodsSkuPo.getInventory();
        Long originalPriceEnc = goodsSkuPo.getOriginalPrice() == null ? null : goodsSkuPo.getOriginalPrice();
        this.id = skuId;
        this.name = nameEnc;
        this.skuSn = skuSnEnc;
        this.imageUrl = imageUrlEnc;
        this.originalPrice = originalPriceEnc;
        this.inventory = inventoryEnc;
        this.price = originalPriceEnc;
        this.disabled = goodsSkuPo.getDisabled() == 0;
    }

    public SkuOutputVo() {
    }
}
