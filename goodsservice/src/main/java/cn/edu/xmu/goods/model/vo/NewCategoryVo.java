package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "类目详细信息")
public class NewCategoryVo {
    private String name;
}
