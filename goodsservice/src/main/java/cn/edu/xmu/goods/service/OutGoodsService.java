package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.OutDao;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.GoodsSkuInfo;
import cn.edu.xmu.otherinterface.bo.ShopInfo;
import cn.edu.xmu.otherinterface.service.GoodsModuleService;
import cn.edu.xmu.otherinterface.service.IGoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(version = "0.0.1")
public class OutGoodsService implements IGoodsService, GoodsModuleService {
    @Autowired
    OutDao outDao;

    @Override
    public Boolean deleteFreightModelId(Long freightModelId, Long ShopId) {
        return outDao.deleteFreightModelId(freightModelId, ShopId);
    }

    @Override
    public ShopInfo getShopInfo(Long shopId) {
        return outDao.getShopInfo(shopId);
    }

    @Override
    public ReturnObject<List<Long>> getShopSkuId(Long shopId) {
        return outDao.getShopSkuId(shopId);
    }

    @Override
    public ReturnObject<GoodsSkuInfo> getSkuInfo(Long goodsSkuId) {
        return outDao.getSkuInfo(goodsSkuId);
    }

}
