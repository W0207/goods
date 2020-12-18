package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.OutDao;
import cn.edu.xmu.otherinterface.bo.GoodInfo;
import cn.edu.xmu.otherinterface.bo.GoodsFreightInfo;
import cn.edu.xmu.otherinterface.bo.MyReturn;
import cn.edu.xmu.otherinterface.bo.ShopInfo;
import cn.edu.xmu.otherinterface.service.GoodsModuleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(version = "0.0.1")
public class OutOrderService implements GoodsModuleService {
    @Autowired
    OutDao outDao;

    @Override
    public Boolean deleteFreightModelId(Long freightModelId, Long ShopId) {
        return outDao.deleteFreightModelId(freightModelId, ShopId);
    }

    @Override
    public MyReturn<ShopInfo> getShopInfo(Long shopId) {
        return outDao.getShopInfo(shopId);
    }

    @Override
    public MyReturn<GoodInfo> getFreightModelIdBySkuId(Long goodSkuId) {
        return outDao.getFreightModelIdBySkuId(goodSkuId);
    }

    @Override
    public MyReturn<List<GoodsFreightInfo>> getGoodsInfoBySkuId(List<Long> skuIds) {
        return outDao.getGoodsInfoBySkuId(skuIds);
    }
}
