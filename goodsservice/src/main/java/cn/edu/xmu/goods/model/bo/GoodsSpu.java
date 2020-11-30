package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.encript.AES;
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

    /**
     * 构造函数
     *
     * @param goodsSpuPo Po对象
     */
    public GoodsSpu(GoodsSpuPo goodsSpuPo) {
        this.id = goodsSpuPo.getId();
        this.name = goodsSpuPo.getName();
        this.brandId = goodsSpuPo.getBrandId();
        this.categoryId = goodsSpuPo.getCategoryId();
        this.freightId = goodsSpuPo.getFreightId();
        this.shopId = goodsSpuPo.getShopId();
        this.goodsSn = goodsSpuPo.getGoodsSn();
        this.detail = goodsSpuPo.getDetail();
        this.imagUrl = goodsSpuPo.getImageUrl();
        this.state = State.getTypeByCode(goodsSpuPo.getState().intValue());
        this.spec = goodsSpuPo.getSpec();
        this.disabled = goodsSpuPo.getDisabled() == 1;
        this.gmtCreated = goodsSpuPo.getGmtCreate();
        this.gmtModified = goodsSpuPo.getGmtModified();
    }

    /**
     * 用 Vo 对象创建 用来更新 GoodsSpu 的 Po 对象
     *
     * @param spuInputVo vo 对象
     * @return po 对象
     */
    public GoodsSpuPo createUpdatePo(SpuInputVo spuInputVo) {
        String nameEnc = spuInputVo.getName() == null ? null : spuInputVo.getName();
        String descriptionEnc = spuInputVo.getDescription() == null ? null : spuInputVo.getDescription();
        String specsEnc = spuInputVo.getSpecs() == null ? null : spuInputVo.getSpecs();

        GoodsSpuPo goodsSpuPo = new GoodsSpuPo();

        goodsSpuPo.setId(id);
        goodsSpuPo.setName(nameEnc);
        goodsSpuPo.setDetail(descriptionEnc);
        goodsSpuPo.setSpec(specsEnc);
        goodsSpuPo.setGmtModified(LocalDateTime.now());
        return goodsSpuPo;
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