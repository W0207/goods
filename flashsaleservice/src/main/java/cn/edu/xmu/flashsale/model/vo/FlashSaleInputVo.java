package cn.edu.xmu.flashsale.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
@ApiOperation(value = "可修改的秒杀活动信息")
public class FlashSaleInputVo {

    private LocalDateTime flashDate;

}
