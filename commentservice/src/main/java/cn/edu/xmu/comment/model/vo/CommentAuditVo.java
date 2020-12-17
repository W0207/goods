package cn.edu.xmu.comment.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "可修改的评论信息")
public class CommentAuditVo {

    @ApiModelProperty(value = "评论状态")
    private boolean state;


    public boolean getState() {
        return state;
    }
}
