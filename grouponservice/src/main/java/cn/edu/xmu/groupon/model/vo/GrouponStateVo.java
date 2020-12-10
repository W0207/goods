package cn.edu.xmu.groupon.model.vo;


import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.bo.GrouponActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品spu状态VO
 */
@Data
public class GrouponStateVo {

    @ApiModelProperty("状态码")
    private Long code;

    @ApiModelProperty("状态名字")
    private String name;

    public GrouponStateVo(GrouponActivity.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }
}
