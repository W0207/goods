package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.*;
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

    public MyReturn<GoodInfo> getFreightModelIdBySkuId(Long goodSkuId) {
        GoodsSkuPo po = new GoodsSkuPo();
        po = skuPoMapper.selectByPrimaryKey(goodSkuId);
        if(po==null){
            return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            //通过sku获取spu
            GoodsSpuPo goodsSpuPo = spuPoMapper.selectByPrimaryKey(po.getGoodsSpuId());
            GoodInfo goodInfo = new GoodInfo(goodsSpuPo.getFreightId(),po.getWeight());
            if(goodsSpuPo==null){
                return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new MyReturn<>(goodInfo);
        }

    }
    public Boolean deleteFreightModelId(Long freightModelId, Long ShopId) {
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(ShopId);
        criteria.andFreightIdEqualTo(freightModelId);
        try {
            List<GoodsSpuPo> pos = spuPoMapper.selectByExample(example);
            for(GoodsSpuPo po: pos){
                po.setFreightId(null);
                spuPoMapper.updateByPrimaryKey(po);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public MyReturn<ShopInfo> getShopInfo(Long shopId) {
        try {
            System.out.println("cuieb");
            ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);
            if (shopPo == null) {
                return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            ShopInfo shopInfo = new ShopInfo(shopPo.getId(), shopPo.getName(), shopPo.getState(), shopPo.getGmtCreate(), shopPo.getGmtModified());
            return new MyReturn<>(shopInfo);
        } catch (Exception e){
            return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误%S",e.getMessage()));
        }
    }

    public MyReturn<List<Long>> getShopSkuId(Long shopId) {
        try{
            MyReturn returnObject =  null;
            //查询shopId是否存在
            ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);
            if(shopPo == null){
                returnObject = new MyReturn(ResponseCode.RESOURCE_ID_NOTEXIST,"shopId不存在");
            } else {
                GoodsSpuPoExample spuPoExample = new GoodsSpuPoExample();
                GoodsSpuPoExample.Criteria criteria = spuPoExample.createCriteria();
                criteria.andShopIdEqualTo(shopId);
                List<GoodsSpuPo> spuPos = spuPoMapper.selectByExample(spuPoExample);
                List<Long> skuIds = new ArrayList<>();
                for(GoodsSpuPo spuPo:spuPos) {
                    GoodsSkuPoExample example = new GoodsSkuPoExample();
                    GoodsSkuPoExample.Criteria criteria1 = example.createCriteria();
                    criteria1.andGoodsSpuIdEqualTo(spuPo.getId());
                    List<GoodsSkuPo> skuPos = skuPoMapper.selectByExample(example);
                    for(GoodsSkuPo po:skuPos){
                        skuIds.add(po.getId());
                    }
                }
                returnObject = new MyReturn(skuIds);
            }
            return returnObject;
        } catch (Exception e){
            return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误%S",e.getMessage()));
        }
    }

    public MyReturn<GoodsSkuInfo> getSkuInfo(Long goodsSkuId) {
        MyReturn returnObject = null;
        try{
            GoodsSkuPo po = skuPoMapper.selectByPrimaryKey(goodsSkuId);
            if(po == null){
                returnObject = new MyReturn(ResponseCode.RESOURCE_ID_NOTEXIST,"skuId不存在");
            } else {
                String spuName = spuPoMapper.selectByPrimaryKey(po.getGoodsSpuId()).getName();
                GoodsSkuInfo goodsSkuInfo = new GoodsSkuInfo(po.getId(),po.getName(),spuName,po.getSkuSn(),po.getImageUrl(),po.getInventory(),po.getOriginalPrice(),goodsDao.getPrice(goodsSkuId)==null? po.getOriginalPrice():goodsDao.getPrice(goodsSkuId),false);
                returnObject = new MyReturn(goodsSkuInfo);
            }
            return returnObject;
        } catch (Exception e) {
            return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误%S",e.getMessage()));
        }
    }

    public MyReturn<List<Long>> getSkuIdList(Long spuId) {
        GoodsSpuPo goodsSpuPo = spuPoMapper.selectByPrimaryKey(spuId);
        if(goodsSpuPo==null){
            return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(spuId);
        criteria.andDisabledEqualTo((byte)0);
        List<GoodsSkuPo> pos = skuPoMapper.selectByExample(example);
        try {
            List skuList = new ArrayList(pos.size());
            for (GoodsSkuPo po : pos) {
                skuList.add(po.getId());
            }
            return new MyReturn<>(skuList);
        } catch (Exception e){
            return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误%S",e.getMessage()));
        }
    }

    public MyReturn<Boolean> inSameSpu(Long sku1, Long sku2) {
        try {
            GoodsSkuPo skuPo1 = skuPoMapper.selectByPrimaryKey(sku1);
            GoodsSkuPo skuPo2 = skuPoMapper.selectByPrimaryKey(sku2);
            if (skuPo1 == null) {
                return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if (skuPo2 == null) {
                return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if (skuPo1.getGoodsSpuId().equals(skuPo2.getGoodsSpuId())) {
                return new MyReturn<>(true);
            }
            return new MyReturn<>(false);
        } catch (Exception e){
            return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public MyReturn<List<GoodsFreightInfo>> getGoodsInfoBySkuId(List<Long> skuIds) {
        List<GoodsFreightInfo> infos = new ArrayList<>(skuIds.size());
        for(Long skuId:skuIds){
            GoodsSkuPo po = skuPoMapper.selectByPrimaryKey(skuId);
            GoodsSpuPo spuPo = spuPoMapper.selectByPrimaryKey(po.getGoodsSpuId());
            GoodsFreightInfo info = new GoodsFreightInfo(skuId,spuPo.getFreightId());
            infos.add(info);
        }
        return new MyReturn<>(infos);
    }


}
