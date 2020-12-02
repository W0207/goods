package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@ApiOperation(value="类目详细信息")
@Data
public class CategoryInputVo {
    @ApiModelProperty(value="类目名称")
    private String name;
}
