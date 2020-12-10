package cn.edu.xmu.groupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Data
@ApiOperation(value = "spu信息")
public class SimpleSpuVo {


    private Long id;

    private String name;

    private String imageUrl;

    private String gmtCreat;

    private String gmtModified;

    private Boolean disable;

    public SimpleSpuVo(){}


}
