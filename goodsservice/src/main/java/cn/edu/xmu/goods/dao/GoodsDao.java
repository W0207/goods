package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPoExample;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
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
        logger.debug("findGoodsSkuById : id=" + id);
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
        return goodsSkuPo;
    }

    /**
     * @param spuId
     * @param spuInputVo
     * @author shibin zhan
     */
    public ReturnObject<Object> modifySpuById(Long spuId, SpuInputVo spuInputVo) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (goodsSpuPo == null || (goodsSpuPo.getState() != null && GoodsSpu.State.getTypeByCode(goodsSpuPo.getState().intValue()) == GoodsSpu.State.DELETED)) {
            logger.info("商品不存在或已被删除：id = " + spuId);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpu goodsSpu = new GoodsSpu(goodsSpuPo);
        GoodsSpuPo po = goodsSpu.createUpdatePo(spuInputVo);

        ReturnObject<Object> returnObject;
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("商品不存在或已被删除：spu id = " + spuId);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spu id = " + spuId + " 的信息已更新");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * @param spuId
     * @return ReturnObject
     * @author shibin zhan
     */
    public ReturnObject<Object> deleteSpuById(Long spuId) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (goodsSpuPo == null || goodsSpuPo.getDisabled() == 1) {
            logger.info("商品不存在或已被删除：id = " + spuId);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpu goodsSpu = new GoodsSpu(goodsSpuPo);
        GoodsSpuPo po = goodsSpu.createDeletePo();
        ReturnObject<Object> returnObject;
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("商品不存在或已被删除：spuId = " + spuId);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spu id = " + spuId + "已删除");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }
}
