package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import io.swagger.annotations.ApiModelProperty;

public class CommentStateVo {

    @ApiModelProperty("状态码")
    private Long code;

    @ApiModelProperty("用户名称")
    private String name;

    public CommentStateVo(Comment.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }

    public String getName() {
        return name;
    }

    public Long getCode() {
        return code;
    }
}
