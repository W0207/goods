package cn.edu.xmu.coupon.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BiuBiuBiu
 */
@Data
@ApiModel(description = "可修改的优惠券活动信息")
public class CouponActivitySkuInputVo {

    private Long id;

    private Long activity_id;

    private Long sku_id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public Long getId() {
        return id;
    }

    public Long getActivity_id() {
        return activity_id;
    }

    public Long getSku_id() {
        return sku_id;
    }
}
