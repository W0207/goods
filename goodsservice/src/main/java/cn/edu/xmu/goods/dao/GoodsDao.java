package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPoExample;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPoExample;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDao {

    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    /**
     * @param id
     * @return GoodsSkuPo
     * @author shibin zhan
     */
    public GoodsSkuPo findGoodsSkuById(Long id) {
        GoodsSkuPoExample goodsSkuPoExample = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = goodsSkuPoExample.createCriteria();
        criteria.andIdEqualTo(id);
        logger.debug("findGoodsSkuById : skuId=" + id);
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
        return goodsSkuPo;
    }

    /**
     * @param id
     * @return GoodsSpuPo
     */
    public GoodsSpuPo findGoodsSpuById(Long id) {
        GoodsSpuPoExample example = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        logger.debug("findSpuById : spuId=" + id);
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(id);
        return goodsSpuPo;
    }

    /**
     * @param spuId
     * @param spuInputVo
     * @author shibin zhan
     */
    public ReturnObject<Object> modifySpuById(Long spuId, SpuInputVo spuInputVo) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (goodsSpuPo == null || (goodsSpuPo.getState() != null && GoodsSpu.State.getTypeByCode(goodsSpuPo.getState().intValue()) == GoodsSpu.State.DELETED)) {
            logger.info("商品不存在或已被删除：spuId = " + spuId);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpu goodsSpu = new GoodsSpu(goodsSpuPo);
        GoodsSpuPo po = goodsSpu.createUpdatePo(spuInputVo);

        ReturnObject<Object> returnObject;
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("商品不存在或已被删除：spuId = " + spuId);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spuId = " + spuId + " 的信息已更新");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * @param spuId 店铺id
     * @param code  spu状态码
     * @return ReturnObject
     * @author shibin zhan
     */
    public ReturnObject<Object> updateGoodsSpuState(Long spuId, Long code) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (goodsSpuPo == null || goodsSpuPo.getDisabled() != 4) {
            logger.info("商品不存在或已被删除：spuId = " + spuId);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpu goodsSpu = new GoodsSpu(goodsSpuPo);
        GoodsSpuPo po = goodsSpu.createUpdateStatePo(code);
        ReturnObject<Object> returnObject;
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("商品不存在或已被删除：spuId = " + spuId);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            if (code == 4) {
                logger.info("spuId = " + spuId + "已上架");
            } else if (code == 0) {
                logger.info("spuId = " + spuId + "已下架");
            } else if (code == 6) {
                logger.info("spuId = " + spuId + "已删除");
            }
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * @param skuId
     * @return ReturnObject
     * @author shibin zhan
     */
    public ReturnObject<Object> deleteGoodsSkuState(Long skuId) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 4) {
            logger.info("spuId = " + skuId + "不存在或已被删除");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createDeleteStatePo();
        ReturnObject<Object> returnObject;
        int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(po);
        if (ret == 0) {
            logger.info("spuId = " + skuId + "不存在或已被删除");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("skuId = " + skuId + "已删除");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }
}
