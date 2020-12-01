package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
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
        return goodsDao.deleteGoodsSkuState(skuId);
    }

    public ReturnObject<PageInfo<VoObject>> findSkuSimple(Integer shopId, String skuSn, Integer page, Integer pageSize, String spuId, String skuSn1, String spuSn)
    {
        ReturnObject<PageInfo<VoObject>> returnObject = goodsDao.findSkuSimple(shopId,skuSn,page,pageSize,spuId,skuSn1,spuSn);
        return returnObject;
    }
}
