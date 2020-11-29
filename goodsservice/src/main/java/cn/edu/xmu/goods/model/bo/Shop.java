package cn.edu.xmu.goods.model.bo;


import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.newShopRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Shop {

    private Long id;

    private String name;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private State state = State.NEW;

    public void setId(Long id) {
        this.id=id;
    }

    /**
     * 店铺状态
     */
    public enum State {
        NEW(0, "新注册"),
        NORM(1, "正常"),
        FORBID(2, "封禁"),
        DELETE(3, "废弃");

        private static final Map<Integer, Shop.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Shop.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Shop.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 构造函数
     * 用po构造
     */
    public Shop(ShopPo po)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.gmtCreate=po.getGmtCreated();
        this.gmtModified=po.getGmtModified();
    }

    public Shop(String name)
    {
        this.name=name;
    }
    public Shop()
    {

    }

    public ShopPo getShopPo(){
        ShopPo shopPo= new ShopPo();
        shopPo.setId(this.id);
        shopPo.setName(this.name);
        shopPo.setGmtCreated(this.gmtCreate);
        shopPo.setGmtModified(this.gmtModified);
        Byte state = (byte) this.state.code;
        shopPo.setState(state);
        return  shopPo;
    }

    /*public newShopRetVo getShopRetPo(){
        newShopRetVo vo= new newShopRetVo();
        vo.setId(this.id);
        vo.setName(this.name);
        vo.setGmtCreated(this.gmtCreate);
        vo.setGmtModified(this.gmtModified);
        return  vo;
    }*/


}
