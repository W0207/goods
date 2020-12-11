package cn.edu.xmu.coupon.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CouponAddLimitVo {
    ArrayList<Long> skuIds;

    public ArrayList<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(ArrayList<Long> skuIds) {
        this.skuIds = skuIds;
    }
}
