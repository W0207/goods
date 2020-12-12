package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.GoodsSkuInfo;
import cn.edu.xmu.otherinterface.bo.ShopInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OutDao {

    @Autowired
    GoodsSpuPoMapper spuPoMapper;

    @Autowired
    ShopPoMapper shopPoMapper;

    @Autowired
    GoodsSkuPoMapper skuPoMapper;

    @Autowired
    GoodsDao goodsDao;

    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    public Boolean deleteFreightModelId(Long freightModelId, Long ShopId) {
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(ShopId);
        criteria.andFreightIdEqualTo(freightModelId);
        try {
            List<GoodsSpuPo> pos = spuPoMapper.selectByExample(example);
            for (GoodsSpuPo po : pos) {
                po.setFreightId(null);
                spuPoMapper.updateByPrimaryKey(po);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ShopInfo getShopInfo(Long shopId) {
        try {
            ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);
            if (shopPo.equals(null)) {
                return new ShopInfo("Success");
            }
            ShopInfo shopInfo = new ShopInfo(shopPo.getId(), shopPo.getName(), shopPo.getState(), shopPo.getGmtCreate(), shopPo.getGmtModified(), "Success");
            return shopInfo;
        } catch (Exception e) {
            return new ShopInfo("Error");
        }
    }

    public ReturnObject<List<Long>> getShopSkuId(Long shopId) {
        try {
            ReturnObject returnObject = null;
            //查询shopId是否存在
            ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);
            if (shopPo.equals(null)) {
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "shopId不存在");
            } else {
                GoodsSpuPoExample spuPoExample = new GoodsSpuPoExample();
                GoodsSpuPoExample.Criteria criteria = spuPoExample.createCriteria();
                criteria.andShopIdEqualTo(shopId);
                List<GoodsSpuPo> spuPos = spuPoMapper.selectByExample(spuPoExample);
                List<Long> skuIds = new ArrayList<>();
                for (GoodsSpuPo spuPo : spuPos) {
                    GoodsSkuPoExample example = new GoodsSkuPoExample();
                    GoodsSkuPoExample.Criteria criteria1 = example.createCriteria();
                    criteria1.andGoodsSpuIdEqualTo(spuPo.getId());
                    List<GoodsSkuPo> skuPos = skuPoMapper.selectByExample(example);
                    for (GoodsSkuPo po : skuPos) {
                        skuIds.add(po.getId());
                    }
                }
                returnObject = new ReturnObject(skuIds);
            }
            return returnObject;
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误%S", e.getMessage()));
        }
    }

    public ReturnObject<GoodsSkuInfo> getSkuInfo(Long goodsSkuId) {
        ReturnObject returnObject = null;
        try {
            GoodsSkuPo po = skuPoMapper.selectByPrimaryKey(goodsSkuId);
            if (po.equals(null)) {
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "skuId不存在");
            } else {
                String spuName = spuPoMapper.selectByPrimaryKey(po.getGoodsSpuId()).getName();
                GoodsSkuInfo goodsSkuInfo = new GoodsSkuInfo(po.getId(), po.getName(), spuName, po.getSkuSn(), po.getImageUrl(), po.getInventory(), po.getOriginalPrice(), goodsDao.getPrice(goodsSkuId).equals(null) ? po.getOriginalPrice() : goodsDao.getPrice(goodsSkuId), false);
                returnObject = new ReturnObject(goodsSkuInfo);
            }
            return returnObject;
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误%S", e.getMessage()));
        }
    }
}
