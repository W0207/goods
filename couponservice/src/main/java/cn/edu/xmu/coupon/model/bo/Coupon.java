package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 菜鸡骞
 */
@Data
public class Coupon implements VoObject {

    public enum State {
        UNPUBLISHED(0, "未领取"),
        GATED(1, "已领取"),
        USED(2,"已使用"),
        DELETED(3, "已失效");

        private static final Map<Integer, Coupon.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Coupon.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Coupon.State getTypeByCode(Integer code) {
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
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }


}
