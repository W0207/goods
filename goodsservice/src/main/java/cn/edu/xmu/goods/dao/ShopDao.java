package cn.edu.xmu.goods.dao;


import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.ShopAuditVo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.ininterface.service.DisableCouponActivityService;
import cn.edu.xmu.ininterface.service.DisableFlashActivityService;
import cn.edu.xmu.ininterface.service.DisableGrouponActivityService;
import cn.edu.xmu.ininterface.service.DisablePresaleActivityService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 宇
 */
@Repository
public class ShopDao {

    @Autowired
    ShopPoMapper shopPoMapper;

    @Autowired
    GoodsSkuPoMapper skuPoMapper;

    @Autowired
    GoodsSpuPoMapper spuPoMapper;

//    @DubboReference(version = "0.0.1", check = false)
//    DisableCouponActivityService disableCouponActivity;
//
//    @DubboReference(version = "0.0.1", check = false)
//    DisableFlashActivityService disableFlashActivityService;
//
//    @DubboReference(version = "0.0.1", check = false)
//    DisablePresaleActivityService disablePresaleActivityService;
//
//    @DubboReference(version = "0.0.1", check = false)
//    DisableGrouponActivityService disableGrouponActivityService;


    private static final Logger logger = LoggerFactory.getLogger(ShopDao.class);

    public ShopPo findShopById(Long id) {
        return shopPoMapper.selectByPrimaryKey(id);
    }

    /**
     * 增加一个店铺
     * <p>
     * by 宇
     */
    public ReturnObject insertShop(Shop shop) {
        ShopPo shopPo = shop.getShopPo();
        ReturnObject<Shop> returnObject = null;
//        ShopPoExample example = new ShopPoExample();
//        ShopPoExample.Criteria criteria = example.createCriteria();
//        criteria.andNameEqualTo(shopPo.getName());
//        List<ShopPo> pos = shopPoMapper.selectByExample(example);
//        if(pos.size()!=0){
//            return new ReturnObject<>(ResponseCode.USER_HASSHOP);
//        }
        try {
            int ret = shopPoMapper.insertSelective(shopPo);
            if (ret == 0) {
                //插入失败
                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            } else {
                //插入成功
                shop.setId(shopPo.getId());
                ShopRetVo shopRetVo = new ShopRetVo(shop);
                returnObject = new ReturnObject(shopRetVo);
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;
    }

    /**
     * 修改店铺信息
     * <p>
     * by 宇
     */
    public ReturnObject<Shop> updateShop(Shop shop) {
        if(shop.getName().isEmpty()||shop.getName().trim().isEmpty()){
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
        ShopPo shopPo = shop.getShopPo();
        ReturnObject<Shop> returnObject = null;
        try {
            ShopPo po = shopPoMapper.selectByPrimaryKey(shopPo.getId());
            if(po==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if(po.getState().equals((byte)4)||po.getState().equals((byte)3)){
                return new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW);
            }
            int ret = shopPoMapper.updateByPrimaryKeySelective(shopPo);
            if (ret == 0) {
                //修改失败
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺id不存在：" + shopPo.getId()));

            } else {
                //修改成功
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            return returnObject;
        } catch (Exception e) {
            // 其他Exception错误
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
            return returnObject;
        }

    }

    /**
     * 删除所有活动
     */
//    public boolean disableAllActivity(Long shopId){
//
//        disableCouponActivity.disableActivity(shopId);
//        GoodsSpuPoExample example = new GoodsSpuPoExample();
//        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
//        criteria.andShopIdEqualTo(shopId);
//        List<GoodsSpuPo> pos = spuPoMapper.selectByExample(example);
//        for(GoodsSpuPo po :pos){
//            GoodsSkuPoExample poExample = new GoodsSkuPoExample();
//            GoodsSkuPoExample.Criteria criteria1 = poExample.createCriteria();
//            criteria1.andGoodsSpuIdEqualTo(po.getId());
//            List<GoodsSkuPo> skuPos = skuPoMapper.selectByExample(poExample);
//            for(GoodsSkuPo po1 : skuPos){
//                disableFlashActivityService.disableActivity(po1.getId());
//            }
//        }
//        disablePresaleActivityService.disableActivity(shopId);
//        disableGrouponActivityService.disableActivity(shopId);
//        return false;
//    }

    /**
     * 上下线商店
     * by 宇
     */
    public ReturnObject<Shop> shopShelves(Shop shop) {
        ShopPo shopPo = shop.getShopPo();

        ReturnObject returnObject = null;
        try {
            ShopPo shopPoSelect = shopPoMapper.selectByPrimaryKey(shopPo.getId());

            if (shopPoSelect == null) {
                //店铺id不存在
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺id不存在：" + shopPo.getId()));
            } else {
                Shop shopSelect = new Shop(shopPoSelect);
                if (shopPo.getState().equals((byte)3) || shopPo.getState().equals((byte)4) || shopPo.getState().equals((byte)0)) {
                    logger.info(shopSelect.getState().getDescription() + "当前状态无法进行变迁");
                    returnObject = new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW, String.format("店铺不允许转换" + shopPo.getId()));
                } else {
                    //可以修改状态
                    //状态一样不予操作，直接返回
                    if(shopPo.getState().equals(shopPoSelect.getState())){
                        return new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW);
                    }
                    if(shopPo.getState().equals((byte)1)){
                        //上架状态转为下架需要将活动进行删除
                        //disableAllActivity(shopPo.getId());
                    }
                    shopPoMapper.updateByPrimaryKeySelective(shopPo);
                    returnObject = new ReturnObject<>();
                }
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {

            // 其他Exception错误
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;
    }

    /**
     * 删除店家
     * by 宇
     */
    public ReturnObject<Shop> deleteShop(Shop shop) {
        ShopPo shopPo = shop.getShopPo();
        ReturnObject returnObject = null;
        try {
            ShopPo shopPoSelect = shopPoMapper.selectByPrimaryKey(shopPo.getId());
            if (shopPoSelect == null) {
                //店铺id不存在
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺id不存在：" + shopPo.getId()));
            } else {
                System.out.println(shopPoSelect.toString());
                Shop shopSelect = new Shop(shopPoSelect);
                if (shopPo.getState().equals((byte)0) || shopPo.getState().equals((byte)4)) {
                    logger.info(shopSelect.getState().getDescription() + "对店家id进行了物理删除");
                    int ret = shopPoMapper.deleteByPrimaryKey(shopPo.getId());
                } else {
                    logger.info(shopSelect.getState().getDescription() + "对店家id进行了逻辑删除");
                    shopPoMapper.updateByPrimaryKeySelective(shopPo);
                    //删除店家的spu
                    GoodsSpuPoExample spuPoExample = new GoodsSpuPoExample();
                    GoodsSpuPoExample.Criteria spuPoExampleCriteria = spuPoExample.createCriteria();
                    spuPoExampleCriteria.andShopIdEqualTo(shopPo.getId());
                    spuPoExampleCriteria.andDisabledEqualTo((byte)0);
                    List<GoodsSpuPo> spuPos = spuPoMapper.selectByExample(spuPoExample);
                    for (GoodsSpuPo po:spuPos){
                        //1.将spu设置为disable为true
                        po.setDisabled(((byte)1));
                        po.setGmtModified(LocalDateTime.now());
                        spuPoMapper.updateByPrimaryKey(po);
                        //2.将sku设置为disable为true
                        GoodsSkuPoExample skuPoExample = new GoodsSkuPoExample();
                        GoodsSkuPoExample.Criteria criteria = skuPoExample.createCriteria();
                        criteria.andGoodsSpuIdEqualTo(po.getId());
                        criteria.andDisabledEqualTo((byte)0);
                        List<GoodsSkuPo> skuPos = skuPoMapper.selectByExample(skuPoExample);
                        for(GoodsSkuPo skuPo:skuPos){
                            skuPo.setDisabled((byte)1);
                            skuPo.setGmtModified(LocalDateTime.now());
                            skuPoMapper.updateByPrimaryKey(skuPo);
                        }
                    }
                    //如果处在上线状态进行删除就关闭所有的活动
                    //disableAllActivity(shopPo.getId());
                    returnObject = new ReturnObject<>();
                }
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;

    }

    public ReturnObject<Object> auditShopByID(Long id, ShopAuditVo shopAuditVo) {
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(id);
        if (null == shopPo) {
            logger.info("新店id= " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        logger.debug("state"+shopPo.getState());
        System.out.println("state"+shopPo.getState());
        //shopPo的状态不是为0，为0才审核
        if(shopPo.getState().equals((byte)0)){
            logger.debug("state"+shopPo.getState());
            shopPo.setGmtModified(LocalDateTime.now());
            if(shopAuditVo.getConclusion()){
                shopPo.setState((byte) 1);
            }else {
                shopPo.setState((byte) 4);
            }
            shopPoMapper.updateByPrimaryKeySelective(shopPo);
        }
        else {
            return new ReturnObject<>(ResponseCode.SHOP_STATENOTALLOW);
        }
        logger.info("新店id = " + id + " 的信息已审核");
        return new ReturnObject<>();
    }
}
