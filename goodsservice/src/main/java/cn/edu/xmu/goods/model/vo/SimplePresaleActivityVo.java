package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimplePresaleActivityVo {
    @ApiModelProperty("预售活动id")
    private Long id;

    @ApiModelProperty("预售活动名字")
    private String name;

    @ApiModelProperty("预售活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("开始支付尾款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("预售活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("定金")
    private Integer advancePayPrice;

    @ApiModelProperty("尾款")
    private Integer restPayPrice;
}
