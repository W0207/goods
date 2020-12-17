package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Abin
 */
@Data
@ApiModel(description = "spu详细信息")
public class SpuInputVo {

    private String name;

    private String decription;

    private String specs;
}
