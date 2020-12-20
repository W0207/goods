package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.outer.model.bo.Freight;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Abin
 */
@Data
public class SpuRetVo implements VoObject {

    private Long id;

    private String name;

    private SimpleBrandVo brand;

    private SimpleCategoryVo category;

    private SimpleShopVo shop;

//    private Freight freight;

    private String goodsSn;

    private String detail;

    private String imageUrl;

    private String spec;

    private List<SimpleSkuVo> skuList;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

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
