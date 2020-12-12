package cn.edu.xmu.groupon.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Data
@ApiOperation(value = "spu信息")
public class GoodsSpu {


    private Long id;

    private String name;

    private  String goodsSn;

    private String imageUrl;

    private LocalDateTime gmtCreat;

    private LocalDateTime gmtModified;

    private Boolean disable;

    public GoodsSpu(){}


}
