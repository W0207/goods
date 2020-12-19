package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.otherinterface.bo.GoodsSkuInfo;
import cn.edu.xmu.otherinterface.bo.MyReturn;

import java.util.List;

public interface IGoodsService {

    /**
     * 通过店铺ID获得店铺SKU ID列表
     * 用于条件查询
     *
     * @return 该店铺全部sku列表
     */
    MyReturn<List<Long>> getShopSkuId(Long shopId);


    /**
     * 根据skuId获得sku信息
     */
    MyReturn<GoodsSkuInfo> getSkuInfo(Long goodsSkuId);

    /**
     * 通过spuId找到对应的skuId list
     */
    MyReturn<List<Long>> getSkuIdList(Long spuId);

    /**
     * 两个sku是不是在同一个spu下
     *
     * @param sku1
     * @param sku2
     * @return
     */
    MyReturn<Boolean> inSameSpu(Long sku1, Long sku2);

}

/**
 * 额外：
 * 1.浏览商品时，通过消息队列发送skuId和customerId
 * 2.浏览商品时，判断是否为分享链接(比通常的商品api后多两个query参数：分享者Id"sharerId"，分享Id"shareId"）,调用我们实现的API
 * void createBeShared(Long sharerId,Long customerId,shareId,Long goodsSkuId);
 */