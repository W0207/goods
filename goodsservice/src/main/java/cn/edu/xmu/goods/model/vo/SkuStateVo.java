package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品spu状态VO
 * @author Abin
 */
@Data
public class SkuStateVo {

    @ApiModelProperty("状态码")
    private Long code;

    @ApiModelProperty("状态名字")
    private String name;

    public SkuStateVo(GoodsSku.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }
}
