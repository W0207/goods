package cn.edu.xmu.groupon.model.bo;

import cn.edu.xmu.groupon.model.po.GrouponActivityPo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.groupon.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class GrouponActivity implements VoObject {

    /**
     * 团购活动状态
     */
    public enum State {
        OFFLINE(0, "已下线"),
        ONLINE(1,"已上线"),
        DELETED(2, "已删除");

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

        public static GrouponActivity.State getTypeByCode(Integer code) {
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

    private Long goodsSpu;

    private Long shop;

    private String strategy;

    private Integer state;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public GrouponActivity(){}

    public GrouponActivity (GrouponActivityPo po)
    {

        if(po==null){

        }
        else {
            this.id = po.getId() == null ? null : po.getId();
            this.name = po.getName() == null ? null : po.getName();
            this.goodsSpu = po.getGoodsSpuId() == null ? null : po.getGoodsSpuId();
            this.shop = po.getShopId() == null ? null : po.getShopId();
            this.strategy = po.getStrategy() == null ? null : po.getStrategy();
            this.state = po.getState() == null ? null : po.getState();
            this.beginTime = po.getBeginTime() == null ? null : po.getBeginTime();
            this.endTime = po.getEndTime() == null ? null : po.getEndTime();
            this.gmtCreate = po.getGmtCreate() == null ? null : po.getGmtCreate();
            this.gmtModified = po.getGmtModified() == null ? null : po.getGmtModified();
        }

    }

    public GrouponActivityPo creatUpdatePo(GrouponInputVo grouponInputVo)
    {
        GrouponActivityPo po=new GrouponActivityPo();
         String strategyEnc=grouponInputVo.getStrategy() == null ? null :grouponInputVo.getStrategy();
         LocalDateTime beginTimeEnc=grouponInputVo.getBeginTime() == null ? null :grouponInputVo.getBeginTime();
         LocalDateTime endTimeEnc=grouponInputVo.getEndTime() == null ? null :grouponInputVo.getEndTime();
         po.setId(this.id);
         po.setName(this.name);
         po.setState(this.state);
         po.setShopId(this.shop);
         po.setGoodsSpuId(this.goodsSpu);
         po.setStrategy(strategyEnc);
         po.setBeginTime(beginTimeEnc);
         po.setEndTime(endTimeEnc);
         po.setGmtCreate(this.gmtCreate);
         po.setGmtModified(LocalDateTime.now());
         return po;
    }

    public  GrouponActivityPo createAddPo(Long spuId,GrouponInputVo grouponInputVo,Long shopId){
        GrouponActivityPo po=new GrouponActivityPo();
        String strategyEnc=grouponInputVo.getStrategy() == null ? null :grouponInputVo.getStrategy();
        LocalDateTime beginTimeEnc=grouponInputVo.getBeginTime() == null ? null :grouponInputVo.getBeginTime();
        LocalDateTime endTimeEnc=grouponInputVo.getEndTime() == null ? null :grouponInputVo.getEndTime();
        po.setStrategy(strategyEnc);
        po.setBeginTime(beginTimeEnc);
        po.setEndTime(endTimeEnc);
        po.setGmtModified(LocalDateTime.now());
        po.setGoodsSpuId(spuId);
        po.setShopId(shopId);
        return po;
    }

    @Override
    public Object createVo() {
        return new GrouponSelectVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
