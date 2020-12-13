package cn.edu.xmu.flashsale.model.vo;

import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class FlashSaleItemRetVo {

    private Long id;

    private SkuRetVo SimpleSku;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
