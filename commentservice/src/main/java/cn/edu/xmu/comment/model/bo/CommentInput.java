package cn.edu.xmu.comment.model.bo;

import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.otherinterface.bo.UserInfo;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
public class CommentInput {

    private Long id;

    private UserInfo customer;

    private Long goodsSkuId;

    private Byte type;

    private String content;

    private Byte state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public void setCustomer(UserInfo customer) {
        this.customer = customer;
    }

    public void setGoodsSkuId(Long goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentInput(CommentPo commentPo) {
        this.gmtCreate=LocalDateTime.now();
        this.state=0;
        this.content = commentPo.getContent();
        this.goodsSkuId = commentPo.getGoodsSkuId();
        this.type = commentPo.getType();
        this.id = commentPo.getId();
        this.gmtModified = commentPo.getGmtModified();
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
