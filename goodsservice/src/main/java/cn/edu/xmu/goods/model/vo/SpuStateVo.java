package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品spu状态VO
 */
@Data
public class SpuStateVo {

    @ApiModelProperty("状态码")
    private Long code;

    @ApiModelProperty("状态名字")
    private String name;

    public SpuStateVo(GoodsSpu.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }
}
