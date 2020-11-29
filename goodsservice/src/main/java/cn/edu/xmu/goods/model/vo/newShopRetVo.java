package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "店铺视图对象")
public class newShopRetVo {

    @ApiModelProperty(value = "店铺id")
    private Long id;

    @ApiModelProperty(value = "店铺名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;

    public newShopRetVo(Shop shop)
    {
        this.id =shop.getId();
        this.name = shop.getName();
        this.gmtCreate= shop.getGmtCreate();
        this.gmtModified= shop.getGmtModified();
    }

    public newShopRetVo(){};
}
