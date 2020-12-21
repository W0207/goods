package cn.edu.xmu.flashsale.model.vo;
import cn.edu.xmu.flashsale.model.vo.TimeSegVo;
import lombok.Data;
import cn.edu.xmu.external.bo.TimeSegInfo;
import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class FlashSaleRetVo {
    private Long id;

    private LocalDateTime flashData;

    private TimeSegVo timeSeq;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
