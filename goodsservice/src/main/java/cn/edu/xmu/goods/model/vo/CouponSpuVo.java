package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponSpuVo {

    private Long id;

    private Long activityId;

    @ApiModelProperty("参与活动的商品spu")
    private List<SimpleSpuVo> simpleSpuVoList;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
