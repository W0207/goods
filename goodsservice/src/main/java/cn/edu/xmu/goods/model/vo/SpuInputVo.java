package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "可修改的spu信息")
public class SpuInputVo {

    private String name;

    private String description;

    private String specs;
}
