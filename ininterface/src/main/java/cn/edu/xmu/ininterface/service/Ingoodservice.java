package cn.edu.xmu.ininterface.service;

import cn.edu.xmu.ininterface.service.model.vo.SkuToCouponVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;

/**
 * @author ASUS
 */
public interface Ingoodservice {

    SkuToCouponVo couponActivityFindSku(Long id);

    /**
     * 预售获取sku信息
     *
     * @param id
     * @return
     */
    SkuToPresaleVo presaleFindSku(Long id);

    /**
     * 秒杀获取sku信息
     *
     * @param id
     * @return SkuToFlashVo
     * @author Abin
     */
    SkuToFlashSaleVo flashFindSku(Long id);

    /**
     * sku存在不存在
     *
     * @param skuId
     * @return
     */
    boolean skuExitOrNot(Long skuId);

    /**
     * sku在不在shop
     *
     * @param shopId
     * @param id
     * @return
     */
    boolean skuInShopOrNot(Long shopId, Long id);

}
