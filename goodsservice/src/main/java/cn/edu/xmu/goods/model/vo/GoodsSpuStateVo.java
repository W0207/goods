package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import lombok.Data;

/**
 * 商品spu状态VO
 */
@Data
public class GoodsSpuStateVo {

    private Long code;

    private String name;

    public GoodsSpuStateVo(GoodsSpu.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }
}
