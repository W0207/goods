package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Abin
 */
@Data
@ApiModel(description = "可修改的spu信息")
public class SpuInputVo {

    private Long ShopId;

    private String name;

    private String description;

    private String specs;

    public Long getShopId() {
        return ShopId;
    }

    public void setShopId(Long shopId) {
        ShopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
}
