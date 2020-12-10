package cn.edu.xmu.groupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiOperation(value = "可修改的团购活动信息")
public class GrouponInputVo {

    @ApiModelProperty(value = "团购活动规则")
    private String strategy;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}