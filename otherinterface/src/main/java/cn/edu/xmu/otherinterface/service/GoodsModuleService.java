package cn.edu.xmu.otherinterface.service;


import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.ShopInfo;

/**
 * @author issyu 30320182200070
 * @date 2020/12/11 8:35
 */
public interface GoodsModuleService {

    /**
     *同步删除运费模板id   商品模块
     *订单模块删除一个运费模板，商品中绑定的运费模板id需要同步删除
     * @param freightModelId
     * @param ShopId
     * @return
     */
    Boolean deleteFreightModelId(Long freightModelId,Long ShopId);

    /**
     * 1-11、1-4、1-3 商品模块
     * 查询店铺信息，需要写明 Object
     * @param shopId
     * @return
     */
    ReturnObject<ShopInfo> getShopInfo(Long shopId);
}

