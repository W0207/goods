package cn.edu.xmu.coupon.model.bo;

import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponPo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 菜鸡骞
 */
@Data
public class Coupon implements VoObject {

    private Long id;

    private Long customerId;

    private String name;

    private String couponSn;

    private Byte state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long activityId;

    public Coupon(CouponPo couponPo) {

        this.beginTime=couponPo.getBeginTime();
        this.couponSn=couponPo.getCouponSn();
        this.customerId=couponPo.getCustomerId();
        this.endTime=couponPo.getEndTime();
        this.beginTime=couponPo.getBeginTime();
        this.id=couponPo.getId();
        this.gmtCreate=couponPo.getGmtCreate();
        this.gmtModified=couponPo.getGmtModified();
        this.state=couponPo.getState();
        this.activityId=couponPo.getActivityId();
        this.name=couponPo.getName();

    }

    public CouponPo createCouponUserPo() {

        CouponPo couponPo=new CouponPo();
        couponPo.setId(id);
        couponPo.setState((byte) 2);
        return couponPo;

    }

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

    public Long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public String getName() {
        return name;
    }
}
