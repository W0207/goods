package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class FlashSaleItemRetVo {

    private Long id;

    private ProductRetVo goodsSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
