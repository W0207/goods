package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.*;
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

    @Autowired
    GoodsCategoryPoMapper goodsCategoryPoMapper;

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
     * 管理员新增商品类目
     *
     * @param id
     * @param categoryInputVo
     * @return
     */
    public GoodsCategoryPo addCategoryById(Long id, CategoryInputVo categoryInputVo) {
        GoodsCategoryPo po = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.debug("categoryId = " + id + "不存在");
            return null;
        }
        GoodsCategory goodsCategory = new GoodsCategory();
        GoodsCategoryPo goodsCategoryPo = goodsCategory.createAddPo(id, categoryInputVo);
        int ret = goodsCategoryPoMapper.insertSelective(goodsCategoryPo);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            //检查新增是否成功
            goodsCategoryPo = null;
        } else {
            logger.info("categoryId = " + id + " 的信息已新增成功");
        }
        return goodsCategoryPo;
    }

    /**
     * 修改商品类目
     *
     * @param id              种类 id
     * @param categoryInputVo 类目详细信息
     * @return 返回对象 ReturnObj<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> modifyCategoryById(Long id, CategoryInputVo categoryInputVo) {
        GoodsCategoryPo po = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("商品类目不存在或已被删除：categoryId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsCategory goodsCategory = new GoodsCategory(po);
        GoodsCategoryPo goodsCategoryPo = goodsCategory.createUpdatePo(categoryInputVo);
        int ret = goodsCategoryPoMapper.updateByPrimaryKeySelective(goodsCategoryPo);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            //检查更新是否成功
            logger.info("商品类目不存在或已被删除：goodsCategoryId = " + id);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("categoryId = " + id + " 的信息已更新");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 删除商品类目
     *
     * @param id 种类 id
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> deleteCategoryById(Long id) {
        GoodsCategoryPo po = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("商品类目不存在或已被删除：categoryId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        int ret = goodsCategoryPoMapper.deleteByPrimaryKey(id);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            logger.info("商品类目不存在或已被删除：goodsCategoryId = " + id);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    public ReturnObject modifySpuBySpuPoId(GoodsSpuPo goodsSpuPo) {
        ReturnObject returnObject = null;
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
        if (ret == 0) {
            logger.info("spuId = " + goodsSpuPo.getId() + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spuId" + goodsSpuPo.getId() + "已加入品牌");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    public ReturnObject modifySpuBySpuPo(GoodsSpuPo goodsSpuPo) {
        ReturnObject returnObject = null;
        int ret = goodsSpuPoMapper.updateByPrimaryKey(goodsSpuPo);
        if (ret == 0) {
            logger.info("spuId = " + goodsSpuPo.getId() + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spuId" + goodsSpuPo.getId() + "已加入品牌");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 查询商品分类关系
     *
     * @param id 种类 id
     * @return 返回对象 ReturnObject<List>
     * @author shangzhao翟
     */
    public ReturnObject<List> getCategoryByPid(Long id) {
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andPidEqualTo(id);
        //查看是否有此分类
        GoodsCategoryPo categoryPo = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (categoryPo == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        List<GoodsCategoryPo> goodsCategoryPos = goodsCategoryPoMapper.selectByExample(example);
        List<GoodsCategory> goodsCategories = new ArrayList<>(goodsCategoryPos.size());
        if (goodsCategoryPos == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        for (GoodsCategoryPo po : goodsCategoryPos) {
            goodsCategories.add(new GoodsCategory(po));
        }
        return new ReturnObject<>(goodsCategories);
    }
}
