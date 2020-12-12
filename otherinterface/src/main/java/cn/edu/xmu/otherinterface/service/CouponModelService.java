package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.CouponActivity;

import java.util.List;

public interface CouponModelService {
    /**
     * 获得优惠活动列表
     * 用于填充购物车返回对象
     * @param goodsSkuId skuId
     * @return 优惠活动列表
     */
    ReturnObject<List<CouponActivity>> getCouponActivity(Long goodsSkuId);
}
