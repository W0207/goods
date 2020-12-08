package cn.edu.xmu.coupon.model.vo;

import cn.edu.xmu.coupon.model.bo.Coupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 菜鸡骞
 */
@Data
public class CouponStateVo {
    @ApiModelProperty("状态码")
    private Long code;

    @ApiModelProperty("状态名称")
    private String name;


    public CouponStateVo(Coupon.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }

    public String getName() {
        return name;
    }

    public Long getCode() {
        return code;
    }
}
