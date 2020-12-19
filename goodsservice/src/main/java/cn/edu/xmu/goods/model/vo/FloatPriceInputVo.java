package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
@ApiModel("可修改的商品价格浮动信息")
public class FloatPriceInputVo {

    @ApiModelProperty("浮动价格")
    private Long activityPrice;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("销售数量")
    private Integer quantity;
}
