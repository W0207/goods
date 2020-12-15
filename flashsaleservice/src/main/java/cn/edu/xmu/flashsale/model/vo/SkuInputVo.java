package cn.edu.xmu.flashsale.model.vo;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@ApiOperation(value = "向秒杀活动添加的SKU信息")
public class SkuInputVo {

    Long skuId;

    Long price;

    Integer quantity;

}
