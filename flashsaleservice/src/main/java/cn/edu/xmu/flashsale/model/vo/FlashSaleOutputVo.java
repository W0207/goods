package cn.edu.xmu.flashsale.model.vo;
import cn.edu.xmu.ooad.util.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.flashsale.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
public class FlashSaleOutputVo {

    Long id;

    SimpleSkuVo goodsSku;

    Long price;

    Integer quantity;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

}
