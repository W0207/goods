package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@ApiModel(description = "可修改的品牌信息")
public class BrandInputVo {

    @ApiModelProperty("品牌名字")
    private String name;

    @ApiModelProperty("品牌详细")
    private String detail;
}
