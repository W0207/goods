package cn.edu.xmu.ininterface.service.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author BiuBiuBiu
 */
@Data
@AllArgsConstructor
public class SkuToCouponVo implements Serializable {

    private Long id;

    private String name;

    private String goodsSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Byte disable;

    public SkuToCouponVo() {

    }
}
