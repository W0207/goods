package cn.edu.xmu.groupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleGrouponActivityVo {

    @ApiModelProperty("团购活动id")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;
}
