package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.bo.Shop;
import lombok.Data;

/**
 * 商铺状态
 * @author Abin
 */
@Data
public class ShopStateVo {

    private Long code;

    private String name;

    public ShopStateVo(Shop.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }
}
