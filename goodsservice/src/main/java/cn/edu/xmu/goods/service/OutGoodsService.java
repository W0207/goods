package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.OutDao;
import cn.edu.xmu.otherinterface.bo.*;
import cn.edu.xmu.otherinterface.service.IGoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(version = "0.0.1")
public class OutGoodsService implements IGoodsService {
    @Autowired
    OutDao outDao;

    @Override
    public MyReturn<List<Long>> getShopSkuId(Long shopId) {
        return outDao.getShopSkuId(shopId);
    }

    @Override
    public MyReturn<GoodsSkuInfo> getSkuInfo(Long goodsSkuId) {
        return outDao.getSkuInfo(goodsSkuId);
    }

    @Override
    public MyReturn<List<Long>> getSkuIdList(Long spuId) {
        return outDao.getSkuIdList(spuId);
    }

    @Override
    public MyReturn<Boolean> inSameSpu(Long sku1, Long sku2) {
        return outDao.inSameSpu(sku1, sku2);
    }
}
