package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
/**
 * @author Abin
 */
@Data
public class FlashSaleItemRetVo {

    private Long id;

    private SkuToFlashSaleVo goodsSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
