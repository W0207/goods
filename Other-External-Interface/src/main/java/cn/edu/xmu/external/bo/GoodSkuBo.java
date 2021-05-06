package cn.edu.xmu.external.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodSkuBo {
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private boolean disable;
}
