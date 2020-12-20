package cn.edu.xmu.comment.model.vo;

import cn.edu.xmu.comment.model.bo.Comment;
import cn.edu.xmu.otherinterface.bo.UserInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
@Data
@ApiModel(description = "评论视图对象")
public class CommentRetVo {

    private Long id;

    private Long customerId;

    private Long orderitemId;

    private Long goodsSkuId;

    private Byte type;

    private String content;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private UserInfo customer;


    public CommentRetVo(Comment comment) {
        this.id = comment.getId();
        this.goodsSkuId = comment.getGoods_sku_id();
        this.type = comment.getType();
        this.customerId = comment.getCustomer_id();
        this.content = comment.getContent();
        this.gmtCreate = comment.getGmtCreate();
        this.gmtModified = comment.getGmtModified();
        this.orderitemId = comment.getOrderitem_id();
        this.customer = comment.getUserInfo();
    }
}
