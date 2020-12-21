package cn.edu.xmu.comment.model.bo;

import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.otherinterface.bo.UserInfo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
@Data
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

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public void setId(Long id) {
        this.id = id;
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
