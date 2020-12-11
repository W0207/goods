package cn.edu.xmu.flashsale.model.bo;

import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.ooad.util.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.flashsale.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FlashSale implements VoObject {
    /**
     * 秒杀活动状态
     */
    public enum State {
        CREATED(0, "未开始"),
        PROCESSING(1, "进行中"),
        END(2, "已结束");

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

        public static FlashSale.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    Long id;

    LocalDateTime flashData;

    Long timeSegId;

    String imageUrl;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    public FlashSale(FlashSalePo po) {
        this.id = po.getId() == null ? null : po.getId();
        this.flashData = po.getFlashDate() == null ? null : po.getFlashDate();
        this.timeSegId = po.getTimeSegId() == null ? null : po.getTimeSegId();
        this.gmtCreate = po.getGmtCreate() == null ? null : po.getGmtCreate();
        this.gmtModified = po.getGmtModified() == null ? null : po.getGmtModified();
    }

    public FlashSalePo createUpdatePo(FlashSaleInputVo flashSaleInputVo) {
        FlashSalePo po = new FlashSalePo();
        po.setId(this.id);
        LocalDateTime flashEnc = flashSaleInputVo.getFlashDate() == null ? null : flashSaleInputVo.getFlashDate();
        po.setFlashDate(flashEnc);
        po.setTimeSegId(this.timeSegId);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(LocalDateTime.now());
        return po;
    }

    public FlashSale() {
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
