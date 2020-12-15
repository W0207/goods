package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;

/**
 * @author Abin
 */
@Data
public class ProductRetVo {
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Boolean disable;
}
