package cn.edu.xmu.comment.model.bo;

import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.comment.model.vo.CommentRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Comment implements VoObject {

    private Long id;

    private Long customer_id;

    private Long goods_sku_id;

    private Long orderitem_id;

    private int type;

    private String content;

    private int state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


    public Comment(CommentPo po) {
        this.id = po.getId();
        this.content = po.getContent();
        this.customer_id = po.getCustomerId();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
        this.goods_sku_id = po.getGoodsSkuId();
        this.orderitem_id = po.getOrderitemId();
        this.state = po.getState();
        this.type = po.getType();

    }

    public enum State {
        UNPUBLISHED(0, "未审核"),
        PUBLISHED(1, "评论成功"),
        DELETED(2, "未通过");

        private static final Map<Integer, Comment.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Comment.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Comment.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public Object createVo() {
        return new CommentRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public int getState() {
        return state;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public int getType() {
        return type;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public Long getGoods_sku_id() {
        return goods_sku_id;
    }

    public Long getOrderitem_id() {
        return orderitem_id;
    }

    public String getContent() {
        return content;
    }
}
