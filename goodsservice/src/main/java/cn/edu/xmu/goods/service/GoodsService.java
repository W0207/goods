package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.BrandInputVo;
import cn.edu.xmu.goods.model.vo.SkuInputVo;
import cn.edu.xmu.goods.model.vo.SkuVo;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    BrandDao brandDao;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    /**
     * 获得sku的详细信息
     *
     * @param `id`
     * @return ReturnObject
     */
    public ReturnObject findGoodsSkuById(Long id) {
        ReturnObject returnObject;
        GoodsSkuPo goodsSkuPo = goodsDao.findGoodsSkuById(id);
        if (goodsSkuPo != null) {
            returnObject = new ReturnObject<>(new GoodsSku(goodsSkuPo));
            logger.debug("findGoodsSkuById : " + returnObject);
        } else {
            logger.debug("findGoodsSkuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;
    }

    /**
     * 获得一条商品spu的详细信息
     *
     * @param `id`
     * @return ReturnObject
     */

    public ReturnObject findGoodsSpuById(Long id) {
        ReturnObject returnObject;
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(id);
        if (goodsSpuPo != null) {
            returnObject = new ReturnObject(new GoodsSpu(goodsSpuPo));
            logger.debug("findGoodsSpuById : " + returnObject);
        } else {
            logger.debug("findGoodsSpuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;
    }

    /**
     * @param spuId
     * @param spuInputVo
     * @return ReturnObject
     */
    @Transactional
    public ReturnObject<Object> modifySpuInfo(Long spuId, SpuInputVo spuInputVo) {
        return goodsDao.modifySpuById(spuId, spuInputVo);
    }

    /**
     * @param spuId
     * @return ReturnObject
     */
    public ReturnObject<Object> deleteSpuById(Long spuId) {
        return goodsDao.updateGoodsSpuState(spuId, 6L);
    }

    /**
     * @param spuId
     * @return ReturnObject
     */
    public ReturnObject putGoodsOnSaleById(Long spuId) {
        return goodsDao.updateGoodsSpuState(spuId, 4L);
    }

    /**
     * @param spuId
     * @return ReturnObject
     */
    public ReturnObject putOffGoodsOnSaleById(Long spuId) {
        return goodsDao.updateGoodsSpuState(spuId, 0L);
    }

    /**
     * @param skuId
     * @return ReturnObject
     */
    public ReturnObject deleteSkuById(Long skuId) {
        return goodsDao.deleteGoodsSku(skuId);
    }

    /**
     * @param id
     * @param skuInputVo
     * @return
     */
    public ReturnObject modifySkuInfo(Long id, SkuInputVo skuInputVo) {
        return goodsDao.modifySkuById(id, skuInputVo);
    }

    public ReturnObject<PageInfo<VoObject>> findSkuSimple(Integer shopId, String skuSn, Integer page, Integer pageSize, String spuId, String skuSn1, String spuSn) {
        ReturnObject<PageInfo<VoObject>> returnObject = goodsDao.findSkuSimple(shopId, skuSn, page, pageSize, spuId, skuSn1, spuSn);
        return returnObject;
    }

    /**
     *
     * @param shopId
     * @param spuId
     * @param id
     * @return
     */
    public ReturnObject spuAddBrand(Long shopId,Long spuId,Long id) {
        ReturnObject returnObject = null;
        GoodsSpuPo tempSpu = goodsDao.findGoodsSpuById(spuId);
        if (tempSpu == null) {
            logger.debug("findGoodsSkuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            if (tempSpu.getShopId() == shopId) {
                BrandPo brandPo = brandDao.findBrandById(id);
                if (brandPo != null) {
                    GoodsSpuPo goodsSpuPo=new GoodsSpuPo();
                    goodsSpuPo.setBrandId(id);
                    goodsSpuPo.setId(spuId);
                    returnObject = goodsDao.modifySpuBySpuPo(goodsSpuPo);
                }
                else {
                    logger.debug("findBrandById : Not Found!");
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                }
            }
            else {
                logger.debug("spuAddBrand shopId和这个spu的里的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }
        return returnObject;
    }
    /**
     * @param id
     * @return
     */
    public ReturnObject invalidFloatPriceById(Long id, Long loginUserId) {
        return goodsDao.invalidFloatPriceById(id, loginUserId);
    }

    public ReturnObject modifyBrandInfo(Long id, BrandInputVo brandInputVo) {
        return goodsDao.modifyBrandById(id,brandInputVo);
    }
}
