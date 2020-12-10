package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author BiuBiuBiu
 */
@Data
public class ShopAuditVo {

    @ApiModelProperty(value = "新店状态")
    private byte state;

    public Byte getState() {
        return state;
    }
}
