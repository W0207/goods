package cn.edu.xmu.groupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import java.time.LocalDateTime;

import java.time.LocalDateTime;

@Data
@ApiOperation(value = "shop信息")
public class SimpleShopVo {

    private Long id;

    private String name;
}
