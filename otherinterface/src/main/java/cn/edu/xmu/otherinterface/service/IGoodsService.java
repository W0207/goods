package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.CouponActivity;
import cn.edu.xmu.otherinterface.bo.GoodsSkuInfo;

import java.util.List;

public interface IGoodsService {

    /**
     * 通过店铺ID获得店铺SKU ID列表
     * 用于条件查询
     * @return 该店铺全部sku列表
     */
  ReturnObject<List<Long>> getShopSkuId(Long shopId);



  /**
   * 根据skuId获得sku信息
   */
  ReturnObject<GoodsSkuInfo> getSkuInfo(Long goodsSkuId);



}

/**额外：
 * 1.浏览商品时，通过消息队列发送skuId和customerId
 * 2.浏览商品时，判断是否为分享链接(比通常的商品api后多两个query参数：分享者Id"sharerId"，分享Id"shareId"）,调用我们实现的API
 * void createBeShared(Long sharerId,Long customerId,shareId,Long goodsSkuId);
 */