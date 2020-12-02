package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.BrandPoMapper;
import cn.edu.xmu.goods.mapper.FloatPricePoMapper;
import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.bo.FloatPrice;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.BrandInputVo;
import cn.edu.xmu.goods.model.vo.SkuInputVo;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GoodsDao {

    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;

    @Autowired
    FloatPricePoMapper floatPricePoMapper;

    @Autowired
    BrandPoMapper brandPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    /**
     * 查找sku
     *
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
     * 查找spu
     *
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
     * 修改spu信息
     *
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
     * 修改spu状态
     *
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
     * 逻辑删除sku
     *
     * @param skuId
     * @return ReturnObject
     * @author shibin zhan
     */
    public ReturnObject<Object> deleteGoodsSku(Long skuId) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 4) {
            logger.info("skuId = " + skuId + "不存在或已被删除");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createDeleteStatePo();
        ReturnObject<Object> returnObject;
        int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(po);
        if (ret == 0) {
            logger.info("skuId = " + skuId + "不存在或已被删除");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("skuId = " + skuId + "已删除");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 查看所有sku
     *
     * @param shopId
     * @param skuSn
     * @param page
     * @param pageSize
     * @param spuId
     * @param skuSn1
     * @param spuSn
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> findSkuSimple(Integer shopId, String skuSn, Integer page, Integer pageSize, String spuId, String skuSn1, String spuSn) {
        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        List<GoodsSkuPo> goodsSkuPos;
        try {
            goodsSkuPos = goodsSkuPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(goodsSkuPos.size());
            for (GoodsSkuPo po : goodsSkuPos) {
                GoodsSku sku = new GoodsSku(po);
                ret.add(sku);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<GoodsSkuPo> goodsSkuPoPage = PageInfo.of(goodsSkuPos);
            PageInfo<VoObject> goodsSkuPage = new PageInfo<>(ret);
            goodsSkuPage.setPages(goodsSkuPoPage.getPages());
            goodsSkuPage.setPageNum(goodsSkuPoPage.getPageNum());
            goodsSkuPage.setPageSize(goodsSkuPoPage.getPageSize());
            goodsSkuPage.setTotal(goodsSkuPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("findSkuSimple: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 修改sku
     *
     * @param skuId
     * @param skuInputVo
     * @return
     */
    public ReturnObject<Object> modifySkuById(Long skuId, SkuInputVo skuInputVo) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 4) {
            logger.info("skuId = " + skuId + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createUpdatePo(skuInputVo);

        ReturnObject<Object> returnObject;
        int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("skuId = " + skuId + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("skuId = " + skuId + " 的信息已更新");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 失效商品价格浮动
     *
     * @param id
     * @return
     */
    public ReturnObject<Object> invalidFloatPriceById(Long id, Long loginUserId) {
        FloatPricePo floatPricePo = floatPricePoMapper.selectByPrimaryKey(id);
        if (floatPricePo == null || floatPricePo.getValid() == 1) {
            logger.info("商品价格浮动不存在已失效");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        FloatPrice floatPrice = new FloatPrice(floatPricePo);
        FloatPricePo po = floatPrice.createUpdateStatePo(loginUserId);
        ReturnObject<Object> returnObject;
        int ret = floatPricePoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("商品价格浮动不存在或已被失效：floatPriceId = " + id);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 修改品牌信息
     *
     * @param id
     * @param brandInputVo
     * @return
     */
    public ReturnObject modifyBrandById(Long id, BrandInputVo brandInputVo) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        if (brandPo == null) {
            logger.info("brandId = " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Brand brand = new Brand(brandPo);
        BrandPo po = brand.createUpdatePo(brandInputVo);

        ReturnObject<Object> returnObject;
        int ret = brandPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("brandId = " + id + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("brandId = " + id + " 的信息已更新");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }
}
