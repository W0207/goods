package cn.edu.xmu.comment.model.vo;

import cn.edu.xmu.comment.model.bo.Comment;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 菜鸡骞
 */
public class CommentStateVo {

    @ApiModelProperty("状态码")
    private Long code;

    @ApiModelProperty("状态名称")
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
