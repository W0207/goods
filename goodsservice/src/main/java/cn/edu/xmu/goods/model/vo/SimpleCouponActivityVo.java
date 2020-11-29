package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleCouponActivityVo {

    private Long id;

    private String name;

    private String imageUrl;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer quantity;

    private LocalDateTime couponTime;

}
