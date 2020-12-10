package cn.edu.xmu.presale.model.bo;

import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class PresaleActivity {


    private String name;

    private Long advancePayPrice;

    private Long restPayPrice;

    private Integer quantity;

    private LocalDateTime beginTime;

    private LocalDateTime payTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private State state = State.NEW;


    public enum State {
        //商-(优惠,团购,预售)活动：0：已新建，1：被取消
        NEW(0, "已下架"),
        ONSHELVES(1,"已上架"),
        CLOSE(2, "已取消");

        private static final Map<Integer, PresaleActivity.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (PresaleActivity.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }
        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    public PresaleActivity(PresaleActivityVo presaleActivityVo){
        this.advancePayPrice=presaleActivityVo.getAdvancePayPrice();
        this.beginTime=presaleActivityVo.getBeginTime();
        this.endTime=presaleActivityVo.getEndTime();
        this.name=presaleActivityVo.getName();
        this.payTime=presaleActivityVo.getPayTime();
        this.quantity=presaleActivityVo.getQuantity();
        this.restPayPrice=presaleActivityVo.getRestPayPrice();
    }

}
