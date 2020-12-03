package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVo {

    private Long id;

    @ApiModelProperty("评论的买家")
    private List<SimpleUserVo> simpleUserVo;

    private long goodsSkuId;

    private int type;

    private String content;


    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}