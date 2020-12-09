package cn.edu.xmu.goods.model.vo;

import lombok.Data;

@Data
public class SimpleSkuVo {
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Boolean disable;
}