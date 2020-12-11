package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class FlashSaleRetVo {
    private Long id;

    private LocalDateTime flashData;

    //private TimeSeq timeSeq;(找其他模块要)

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
