package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class GoodsSpu implements VoObject {
    /**
     * 商品状态
     */
    public enum State {
        UNPUBLISHED(0, "未发布"),
        PUBLISHED(1, "发布"),
        DELETED(2, "废弃");

        private static final Map<Integer, State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static GoodsSpu.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    private Long id;

    private String name;

    private Long brandId;

    private Long categoryId;

    private Long freightId;

    private Long shopId;

    private String goodsSn;

    private String detail;

    private String imagUrl;

    private State state = State.UNPUBLISHED;

    private String spec;

    private Boolean disabled;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }


}