package cn.edu.xmu.ininterface.service;

import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;

/**
 * @author ASUS
 */
public interface Ingoodservice {

     Object echo2(Object message);
     SkuToPresaleVo presaleFindSku(Long id);
     boolean skuExitOrNot(Long skuId);
     boolean skuInShopOrNot(Long shopId,Long id);
}
