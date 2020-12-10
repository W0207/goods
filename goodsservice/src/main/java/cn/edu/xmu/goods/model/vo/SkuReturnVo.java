package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class SkuReturnVo implements VoObject {

    private Long id;

    private String name;

    private String skuSn;

    private String detail;

    private String imageUrl;

    private Long originalPrice;

    private Long price;

    private Integer inventory;

    private Byte state;

    private String configuration;

    private Long weight;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SpuRetVo Spu;

    private Boolean disable;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
