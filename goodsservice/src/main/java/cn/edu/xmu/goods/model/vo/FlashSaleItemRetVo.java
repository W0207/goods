package cn.edu.xmu.goods.model.vo;

import java.time.LocalDateTime;

public class FlashSaleItemRetVo {

    private Long id;

    private Long saleId;

    private SkuRetVo goodsSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
