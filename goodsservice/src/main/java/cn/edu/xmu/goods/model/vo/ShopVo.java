package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "商铺视图对象")
public class ShopVo {
    @NotBlank(message = "角色名不能为空")
    @ApiModelProperty(value = "角色名称")
    private String name;

    public Shop createShop()
    {
        Shop shop = new Shop();
        shop.setName(this.name);
        return shop;
    }
}
