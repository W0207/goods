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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Abin
 */
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

    @Autowired
    ShopPoMapper shopPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    /**
     * 已下架
     */
    public static final byte OFFSALE = 0;

    /**
     * 已上架
     */
    public static final byte ONSALE = 4;

    /**
     * 已删除
     */
    public static final byte DELETED = 6;


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
        logger.debug("findGoodsSpuById : spuId=" + id);
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(id);
        return goodsSpuPo;
    }

    /**
     * 上架商品(上架未上架未删除的商品)
     *
     * @param shopId
     * @param skuId
     * @param code
     * @return
     */
    public ReturnObject<Object> putGoodsOnSaleById(Long shopId, Long skuId, Long code) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 0) {
            logger.debug("sku或禁止访问");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = findGoodsSpuById(goodsSkuPo.getGoodsSpuId()).getShopId();
        if (!shopid.equals(shopId)) {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (goodsSkuPo.getState() == DELETED) {
            logger.debug("sku已删除");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (goodsSkuPo.getState() == ONSALE) {
            logger.debug("sku已上架");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createUpdateStatePo(code);
        ReturnObject<Object> returnObject;
        try {
            int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(po);
            // 检查更新有否成功
            if (ret == 0) {
                logger.info("商品不存在或已被删除：skuId = " + skuId);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("skuId = " + skuId + "已上架");
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
    }

    /**
     * 下架商品(下架上架，未删除的商品)
     *
     * @param shopId 店铺id
     * @param skuId  sku id
     * @param code   spu状态码
     * @return ReturnObject
     * @author shibin zhan
     */
    public ReturnObject<Object> putOffGoodsOnSaleById(Long shopId, Long skuId, Long code) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 0) {
            logger.debug("sku或禁止访问");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = findGoodsSpuById(goodsSkuPo.getGoodsSpuId()).getShopId();
        if (!shopid.equals(shopId)) {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (goodsSkuPo.getState() == DELETED) {
            logger.debug("sku已删除");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (goodsSkuPo.getState() == OFFSALE) {
            logger.debug("sku已下架");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createUpdateStatePo(code);
        ReturnObject<Object> returnObject;
        try {
            int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(po);
            // 检查更新有否成功
            if (ret == 0) {
                logger.info("商品不存在或已被删除：skuId = " + skuId);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("skuId = " + skuId + "已下架");
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }

    }

    /**
     * 逻辑删除(删除未上架，未删除)
     *
     * @param shopId
     * @param skuId
     * @param code
     * @return
     */
    public ReturnObject<Object> deleteSkuById(Long shopId, Long skuId, Long code) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 0) {
            logger.debug("sku或禁止访问");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = findGoodsSpuById(goodsSkuPo.getGoodsSpuId()).getShopId();
        if (!shopid.equals(shopId)) {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (goodsSkuPo.getState() == DELETED) {
            logger.debug("sku已删除");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (goodsSkuPo.getState() == ONSALE) {
            logger.debug("sku已上架");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createUpdateStatePo(code);
        ReturnObject<Object> returnObject;
        try {
            int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(po);
            // 检查更新有否成功
            if (ret == 0) {
                logger.info("商品不存在或已被删除：skuId = " + skuId);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("skuId = " + skuId + "已删除");
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
    }

    /**
     * 查看所有sku
     *
     * @param shopId
     * @param skuSn
     * @param page
     * @param pageSize
     * @param spuId
     * @param spuSn
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> findSkuSimple(Long shopId, Integer page, Integer
            pageSize, Long spuId, String skuSn, String spuSn) {

        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = example.createCriteria();
        //spu
        GoodsSpuPoExample spuPoExample = new GoodsSpuPoExample();
        GoodsSpuPoExample.Criteria spuCriteria = spuPoExample.createCriteria();
        //参数判断
        if (skuSn != null) {
            criteria.andSkuSnEqualTo(skuSn);
        }
        if(shopId != null) {
            spuCriteria.andShopIdEqualTo(shopId);
        }
        if(spuSn != null) {
            spuCriteria.andGoodsSnEqualTo(spuSn);
        }
        List<GoodsSpuPo> goodsSpuPos;

        if(spuSn != null || shopId != null) {
            goodsSpuPos = goodsSpuPoMapper.selectByExample(spuPoExample);
        }
        else {
            goodsSpuPos = null;
        }
        Long spuFindId = null;

        if(goodsSpuPos!=null&&!goodsSpuPos.isEmpty()) {
            spuFindId=goodsSpuPos.get(0).getId();
            criteria.andGoodsSpuIdEqualTo(spuFindId);
        }
        if (spuId != null) {
            if(!spuId.equals(spuFindId)&&spuFindId!=null) {
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID,"输入spuId和spu信息查询结果不符");
            }
            criteria.andGoodsSpuIdEqualTo(spuId);
        }

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
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
    }

    /**
     * 修改sku
     *
     * @param shopId
     * @param skuId
     * @param skuInputVo
     * @return
     */
    public ReturnObject<Object> modifySkuById(Long shopId, Long skuId, SkuInputVo skuInputVo) {
        GoodsSkuPo goodsSkuPo;
        try {
            goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 0) {
            logger.info("skuId = " + skuId + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        Long shopid = findGoodsSpuById(goodsSkuPo.getGoodsSpuId()).getShopId();
        if (!shopid.equals(shopId)) {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        GoodsSkuPo po = goodsSku.createUpdatePo(skuInputVo);
        try {
            goodsSkuPoMapper.updateByPrimaryKeySelective(po);
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
        logger.info("skuId = " + skuId + " 的信息已更新");
        return new ReturnObject<>();
    }

    /**
     * 失效商品价格浮动
     *
     * @param id
     * @return ReturnObject<Object>
     * @Author shibin zhan
     */
    public ReturnObject<Object> invalidFloatPriceById(Long shopId, Long id, Long loginUserId) {
        FloatPricePo floatPricePo = floatPricePoMapper.selectByPrimaryKey(id);
        if (floatPricePo == null) {
            logger.info("商品价格浮动不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSkuPo goodsSkuPo = findGoodsSkuById(floatPricePo.getGoodsSkuId());
        if (goodsSkuPo == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "商品价格浮动不存在");
        }
        Long goodsSpuId = goodsSkuPo.getGoodsSpuId();
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(goodsSpuId);
        if (goodsSpuPo == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "商品价格浮动不存在");
        }
        Long shopid = goodsSpuPo.getShopId();
        if (shopid.equals(shopId)) {
            FloatPrice floatPrice = new FloatPrice(floatPricePo);
            FloatPricePo po = floatPrice.createUpdateStatePo(loginUserId);
            floatPricePoMapper.updateByPrimaryKeySelective(po);
            logger.debug("invalidFloatPriceById : shopId = " + shopId + " floatPriceId = " + id + " invalidBy " + loginUserId);
            return new ReturnObject<>(ResponseCode.OK);
        } else {
            logger.debug("error");
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
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
        if (ret == 0) {
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
        if (categoryInputVo.getName() == null) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "商品类目名称不能为空");
        }
        GoodsCategoryPoExample goodsCategoryPoExample = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = goodsCategoryPoExample.createCriteria();
        criteria.andNameEqualTo(categoryInputVo.getName());
        List<GoodsCategoryPo> goodsCategoryPos = goodsCategoryPoMapper.selectByExample(goodsCategoryPoExample);
        if (!goodsCategoryPos.isEmpty()) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "商品类目名称不能重复");
        }

        GoodsCategory goodsCategory = new GoodsCategory(po);
        GoodsCategoryPo goodsCategoryPo = goodsCategory.createUpdatePo(categoryInputVo);
        int ret = goodsCategoryPoMapper.updateByPrimaryKeySelective(goodsCategoryPo);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            //检查更新是否成功
            logger.info("商品类目不存在或已被删除：goodsCategoryId = " + id);
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
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

    /**
     * 修改部分信息
     *
     * @param goodsSpuPo
     * @return
     */
    public ReturnObject modifySpuBySpuPoId(GoodsSpuPo goodsSpuPo) {
        ReturnObject returnObject;
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
        if (ret == 0) {
            logger.info("spuId = " + goodsSpuPo.getId() + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spuId" + goodsSpuPo.getId() + "已修改");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 修改全部信息，针对有些值要设置为null的时候修改部分信息不能将参数传进去修改
     *
     * @param goodsSpuPo
     * @return
     */
    public ReturnObject modifySpuBySpuPo(GoodsSpuPo goodsSpuPo) {
        ReturnObject returnObject = null;
        int ret = goodsSpuPoMapper.updateByPrimaryKey(goodsSpuPo);
        if (ret == 0) {
            logger.info("spuId = " + goodsSpuPo.getId() + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("spuId" + goodsSpuPo.getId() + "已修改");
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
        //查看是否有此分类
        GoodsCategoryPo categoryPo = goodsCategoryPoMapper.selectByPrimaryKey(id);
        if (categoryPo == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsCategoryPoExample example = new GoodsCategoryPoExample();
        GoodsCategoryPoExample.Criteria criteria = example.createCriteria();
        criteria.andPidEqualTo(id);
        List<GoodsCategoryPo> goodsCategoryPos = goodsCategoryPoMapper.selectByExample(example);
        List<GoodsCategory> goodsCategories = new ArrayList<>(goodsCategoryPos.size());
        for (GoodsCategoryPo po : goodsCategoryPos) {
            goodsCategories.add(new GoodsCategory(po));
        }
        return new ReturnObject<>(goodsCategories);
    }

    /**
     * @param id
     * @return
     */
    public GoodsCategoryPo getCategoryById(Long id) {
        return goodsCategoryPoMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有品牌
     *
     * @param page:    页码
     * @param pageSize : 每页数量
     * @return 品牌列表
     */
    public ReturnObject<PageInfo<VoObject>> findAllBrand(Integer page, Integer pageSize) {
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        List<BrandPo> brandPos;
        try {
            brandPos = brandPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(brandPos.size());
            for (BrandPo po : brandPos) {
                Brand bran = new Brand(po);
                ret.add(bran);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<BrandPo> brandPoPage = PageInfo.of(brandPos);
            PageInfo<VoObject> brandPage = new PageInfo<>(ret);
            brandPage.setPages(brandPoPage.getPages());
            brandPage.setPageNum(brandPoPage.getPageNum());
            brandPage.setPageSize(brandPoPage.getPageSize());
            brandPage.setTotal(brandPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("findAllBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 删除品牌(物理删除)
     *
     * @param id
     * @return
     */
    public ReturnObject<Object> deleteBrandById(Long id) {
        try {
            BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
            if (brandPo == null) {
                logger.info("品牌不存在");
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            ReturnObject<Object> returnObject;
            int ret = brandPoMapper.deleteByPrimaryKey(id);
            // 检查更新有否成功
            if (ret == 0) {
                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            } else {
                returnObject = new ReturnObject<>(ResponseCode.OK);
            }
            return returnObject;
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
    }

    /**
     * 修改品牌信息
     *
     * @param shopId
     * @param id
     * @param brandInputVo
     * @return
     */
    public ReturnObject modifyBrandById(Long shopId, Long id, BrandInputVo brandInputVo) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        if (brandPo == null) {
            logger.info("brandId = " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (brandInputVo.getName() == null) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "品牌名称不能为空");
        }
        BrandPoExample brandPoExample = new BrandPoExample();
        BrandPoExample.Criteria criteria = brandPoExample.createCriteria();
        criteria.andNameEqualTo(brandInputVo.getName());
        List<BrandPo> brandPos = brandPoMapper.selectByExample(brandPoExample);
        if (!brandPos.isEmpty()) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "品牌名称不能重复");
        }
        Brand brand = new Brand(brandPo);
        BrandPo po = brand.createUpdatePo(brandInputVo);
        ReturnObject<Object> returnObject;
        int ret = brandPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("brandId = " + id + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } else {
            logger.info("brandId = " + id + " 的信息已更新");
            returnObject = new ReturnObject<>(ResponseCode.OK);
        }
        return returnObject;
    }

    /**
     * 管理员新增品牌
     *
     * @param brandInputVo 品牌详细信息
     * @return BrandPo
     */
    public BrandPo addBrandById(BrandInputVo brandInputVo) {
        Brand brand = new Brand();
        try {
            if (brandInputVo.getName() == null) {
                return null;
            }
            BrandPo brandPo = brand.createAddPo(brandInputVo);
            int ret = brandPoMapper.insertSelective(brandPo);
            if (ret == 0) {
                //检查新增是否成功
                brandPo = null;
            } else {
                logger.info("品牌已新建成功");
            }
            return brandPo;
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return null;
        }
    }

    /**
     * @param id
     * @return
     */
    public BrandPo findBrandById(Long id) {
        return brandPoMapper.selectByPrimaryKey(id);
    }

    /**
     * @param id
     * @return
     */
    public ReturnObject<GoodsSku> getGoodsSkuById(Long id) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
        if (goodsSkuPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
        return new ReturnObject<>(goodsSku);
    }

    /**
     * @param id
     * @return
     */
    public ReturnObject<GoodsSpu> getGoodsSpuById(Long id) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(id);
        if (goodsSpuPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        GoodsSpu goodsSpu = new GoodsSpu(goodsSpuPo);
        return new ReturnObject<>(goodsSpu);
    }

    /**
     * @param id
     * @return
     */
    public ReturnObject<Brand> getBrandById(Long id) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        if (brandPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Brand brand = new Brand(brandPo);
        return new ReturnObject<>(brand);
    }

    /**
     * @param goodsSku
     * @return
     */
    public ReturnObject updateGoodsSkuImgUrl(GoodsSku goodsSku) {
        ReturnObject returnObject = new ReturnObject();
        GoodsSkuPo goodsSkuPo = new GoodsSkuPo();
        goodsSkuPo.setId(goodsSku.getId());
        goodsSkuPo.setImageUrl(goodsSku.getImageUrl());
        int ret = goodsSkuPoMapper.updateByPrimaryKeySelective(goodsSkuPo);
        if (ret == 0) {
            logger.debug("updateGoodsSkuImgUrl: update fail. sku id: " + goodsSku.getId());
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateGoodsSkuImgUrl: update sku success : " + goodsSku.toString());
            returnObject = new ReturnObject();
        }
        return returnObject;
    }

    /**
     * @param goodsSpu
     * @return
     */
    public ReturnObject updateGoodsSpuImgUrl(GoodsSpu goodsSpu) {
        ReturnObject returnObject = new ReturnObject();
        GoodsSpuPo goodsSpuPo = new GoodsSpuPo();
        goodsSpuPo.setId(goodsSpu.getId());
        goodsSpuPo.setImageUrl(goodsSpu.getImageUrl());
        int ret = goodsSpuPoMapper.updateByPrimaryKeySelective(goodsSpuPo);
        if (ret == 0) {
            logger.debug("updateGoodsSpuImgUrl: update fail. spu id: " + goodsSpu.getId());
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateGoodsSpuImgUrl: update sku success : " + goodsSpu.toString());
            returnObject = new ReturnObject();
        }
        return returnObject;
    }

    /**
     * @param brand
     * @return
     */
    public ReturnObject updateBrandImgUrl(Brand brand) {
        ReturnObject returnObject = new ReturnObject();
        BrandPo brandPo = new BrandPo();
        brandPo.setId(brand.getId());
        brandPo.setImageUrl(brand.getImageUrl());
        int ret = brandPoMapper.updateByPrimaryKeySelective(brandPo);
        if (ret == 0) {
            logger.debug("updateBrandImgUrl: update fail. brand id: " + brand.getId());
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateBrandSpuImgUrl: update brand success : " + brand.toString());
            returnObject = new ReturnObject();
        }
        return returnObject;
    }

    /**
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject deleteSpuById(Long shopId, Long id) {
        try {
            GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(id);
            if (goodsSpuPo == null || goodsSpuPo.getDisabled() != 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("不存在或禁止访问");
                }
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            Long shopid = goodsSpuPo.getShopId();
            if (!shopid.equals(shopId)) {
                logger.debug("访问不合法");
                return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
            }
            GoodsSkuPoExample example = new GoodsSkuPoExample();
            GoodsSkuPoExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsSpuIdEqualTo(id);
            List<GoodsSkuPo> goodsSkuPos = goodsSkuPoMapper.selectByExample(example);
            if (goodsSkuPos.size() == 0) {
                goodsSpuPoMapper.deleteByPrimaryKey(id);
                return new ReturnObject();
            } else {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spu不合法");
            }
        } catch (Exception e) {
            logger.error("exception:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误%s", e.getMessage()));
        }
    }

    /**
     * 新增商品价格浮动
     *
     * @param shopId
     * @param id
     * @param floatPriceInputVo
     * @param userId
     * @return
     */
    public ReturnObject addFloatPrice(Long shopId, Long id, FloatPriceInputVo floatPriceInputVo, Long userId) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 0) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long spuId = goodsSkuPo.getGoodsSpuId();
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        if (goodsSpuPo == null || goodsSpuPo.getDisabled() != 0) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = goodsSpuPo.getShopId();
        if (!shopid.equals(shopId)) {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if ("".equals(floatPriceInputVo.getBeginTime())) {
            return new ReturnObject<>(ResponseCode.Log_BEGIN_NULL);
        }
        if ("".equals(floatPriceInputVo.getEndTime())) {
            return new ReturnObject<>(ResponseCode.Log_END_NULL);
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTime = LocalDateTime.parse(floatPriceInputVo.getBeginTime(), df);
        LocalDateTime endTime = LocalDateTime.parse(floatPriceInputVo.getEndTime(), df);
        if (beginTime.isAfter(endTime)) {
            //开始时间不能比结束时间晚
            return new ReturnObject<>(ResponseCode.Log_Bigger);
        } else {
            //设置的库存不能大于总库存
            if (goodsSkuPo.getInventory() < floatPriceInputVo.getQuantity()) {
                logger.debug("库存数量不够");
                return new ReturnObject<>(ResponseCode.SKU_NOTENOUGH);
            } else {
                //一个商品只能有一个价格浮动在使用
                //查找当前时间段重合，生效，且skuId的商品价格浮动
                FloatPricePoExample floatPricePoExample = new FloatPricePoExample();
                FloatPricePoExample.Criteria criteria = floatPricePoExample.createCriteria();
                criteria.andGoodsSkuIdEqualTo(id);
                criteria.andEndTimeGreaterThan(beginTime);
                criteria.andValidEqualTo((byte) 1);
                List<FloatPricePo> floatPricePos = floatPricePoMapper.selectByExample(floatPricePoExample);
                //如果存在，则返回时间段冲突
                if (floatPricePos.size() != 0) {
                    logger.debug("时间段冲突");
                    return new ReturnObject<>(ResponseCode.SKUPRICE_CONFLICT);
                } else {
                    //时间段不冲突则新建一个商品价格浮动并且设为生效
                    FloatPricePo floatPricePo = new FloatPricePo();
                    floatPricePo.setGoodsSkuId(id);
                    floatPricePo.setActivityPrice(floatPriceInputVo.getActivityPrice());
                    floatPricePo.setBeginTime(beginTime);
                    floatPricePo.setEndTime(endTime);
                    floatPricePo.setCreatedBy(userId);
                    floatPricePo.setQuantity(floatPriceInputVo.getQuantity());
                    floatPricePo.setGmtCreate(LocalDateTime.now());
                    floatPricePo.setInvalidBy(userId);
                    floatPricePo.setValid((byte) 1);
                    floatPricePoMapper.insertSelective(floatPricePo);
                    FloatPriceRetVo floatPriceRetVo = new FloatPriceRetVo(floatPricePo);
                    return new ReturnObject(floatPriceRetVo);
                }
            }
        }
    }

    /**
     * 新增商品spu
     *
     * @param shopId
     * @param spuInputVo
     * @return
     */
    public ReturnObject addSpu(Long shopId, SpuInputVo spuInputVo) {
        GoodsSpu goodsSpu = new GoodsSpu();
        GoodsSpuPo goodsSpuPo = goodsSpu.createUpdatePo(shopId, spuInputVo);
        goodsSpuPoMapper.insertSelective(goodsSpuPo);
        SpuRetVo spuRetVo = new SpuRetVo();
        spuRetVo.setId(goodsSpuPo.getId());
        spuRetVo.setSpec(goodsSpuPo.getSpec());
        spuRetVo.setName(goodsSpuPo.getName());
        spuRetVo.setDetail(goodsSpuPo.getDetail());
        spuRetVo.setGoodsSn(goodsSpuPo.getGoodsSn());
        spuRetVo.setImageUrl(goodsSpuPo.getImageUrl());
        spuRetVo.setGmtCreate(goodsSpuPo.getGmtCreate());
        spuRetVo.setGmtModified(goodsSpuPo.getGmtModified());
        spuRetVo.setDisable(false);
        SimpleShopVo simpleShopVo = new SimpleShopVo();
        System.out.println(shopPoMapper.selectByPrimaryKey(shopId).getName());
        if (shopPoMapper.selectByPrimaryKey(shopId) != null) {
            simpleShopVo.setShopId(shopId);
            simpleShopVo.setName(shopPoMapper.selectByPrimaryKey(shopId).getName());
            spuRetVo.setShop(simpleShopVo);
        }
        return new ReturnObject(spuRetVo);
    }

    /**
     * 获得sku详细信息
     *
     * @param id
     * @return
     */
    public ReturnObject getSku(Long id) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 0) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        SkuReturnVo skuReturnVo = new SkuReturnVo();
        skuReturnVo.setId(id);
        skuReturnVo.setName(goodsSkuPo.getName());
        skuReturnVo.setSkuSn(goodsSkuPo.getSkuSn());
        skuReturnVo.setDetail(goodsSkuPo.getDetail());
        skuReturnVo.setImageUrl(goodsSkuPo.getImageUrl());
        skuReturnVo.setOriginalPrice(goodsSkuPo.getOriginalPrice());
        //获得现价
        Long price = getPrice(id);
        if (price == null) {
            skuReturnVo.setPrice(goodsSkuPo.getOriginalPrice());
        } else {
            skuReturnVo.setPrice(price);
        }
        skuReturnVo.setInventory(goodsSkuPo.getInventory());
        skuReturnVo.setState(goodsSkuPo.getState());
        skuReturnVo.setConfiguration(goodsSkuPo.getConfiguration());
        skuReturnVo.setWeight(goodsSkuPo.getWeight());
        skuReturnVo.setGmtCreate(goodsSkuPo.getGmtCreate());
        skuReturnVo.setGmtModified(goodsSkuPo.getGmtModified());
        skuReturnVo.setDisable(goodsSkuPo.getDisabled() != 0);
        SpuRetVo spuRetVo = getSpuRetVo(id);
        skuReturnVo.setSpu(spuRetVo);
        return new ReturnObject(skuReturnVo);
    }


    /**
     * 获得spu的详细信息
     *
     * @param id
     * @return
     */
    public ReturnObject getSpu(Long id) {
        SpuRetVo spuRetVo = getSpuRetVo(id);
        return new ReturnObject(spuRetVo);
    }

    /**
     * 获得现价
     *
     * @param id
     * @return
     */
    public Long getPrice(Long id) {
        //查找有效的价格浮动，记录现价
        LocalDateTime localDateTime = LocalDateTime.now();
        FloatPricePoExample floatPricePoExample = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria = floatPricePoExample.createCriteria();
        criteria.andGoodsSkuIdEqualTo(id);
        criteria.andBeginTimeLessThanOrEqualTo(localDateTime);
        criteria.andEndTimeGreaterThanOrEqualTo(localDateTime);
        criteria.andValidEqualTo((byte) 1);
        List<FloatPricePo> floatPricePos = floatPricePoMapper.selectByExample(floatPricePoExample);
        //如果有合法的价格浮动，记录价格浮动的价格
        if (floatPricePos.isEmpty()) {
            return null;
        } else {
            return floatPricePos.get(0).getActivityPrice();
        }
    }

    /**
     * @param id
     * @return
     */
    public SpuRetVo getSpuRetVo(Long id) {
        SpuRetVo spuRetVo = new SpuRetVo();
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(id);
        spuRetVo.setId(goodsSpuPo.getId());
        spuRetVo.setName(goodsSpuPo.getName());
        //记录品牌信息
        Long brandId = goodsSpuPo.getBrandId();
        if (brandId != null) {
            BrandPo brandPo = brandPoMapper.selectByPrimaryKey(brandId);
            if (brandPo != null) {
                SimpleBrandVo simpleBrandVo = new SimpleBrandVo();
                simpleBrandVo.setId(brandPo.getId());
                simpleBrandVo.setName(brandPo.getName());
                if (brandPo.getImageUrl() != null) {
                    simpleBrandVo.setImageUrl(brandPo.getImageUrl());
                }
                spuRetVo.setBrand(simpleBrandVo);
            }
        }
        //记录分类信息
        Long categoryId = goodsSpuPo.getCategoryId();
        if (categoryId != null) {
            GoodsCategoryPo goodsCategoryPo = goodsCategoryPoMapper.selectByPrimaryKey(categoryId);
            if (goodsCategoryPo != null) {
                SimpleCategoryVo simpleCategoryVo = new SimpleCategoryVo();
                simpleCategoryVo.setId(goodsCategoryPo.getId());
                simpleCategoryVo.setName(goodsCategoryPo.getName());
                spuRetVo.setCategory(simpleCategoryVo);
            }
        }
        //记录店铺信息
        Long shopId = goodsSpuPo.getShopId();
        if (shopId != null) {
            ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);
            if (shopPo != null) {
                SimpleShopVo simpleShopVo = new SimpleShopVo();
                simpleShopVo.setShopId(id);
                simpleShopVo.setName(shopPo.getName());
            }
        }
        spuRetVo.setGoodsSn(goodsSpuPo.getGoodsSn());
        spuRetVo.setDetail(goodsSpuPo.getDetail());
        spuRetVo.setImageUrl(goodsSpuPo.getImageUrl());
        spuRetVo.setSpec(goodsSpuPo.getSpec());
        spuRetVo.setGmtCreate(goodsSpuPo.getGmtCreate());
        spuRetVo.setGmtModified(goodsSpuPo.getGmtModified());
        spuRetVo.setDisable(goodsSpuPo.getDisabled() == 0);
        //记录skuList信息
        GoodsSkuPoExample goodsSkuPoExample = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria1 = goodsSkuPoExample.createCriteria();
        criteria1.andGoodsSpuIdEqualTo(id);
        List<GoodsSkuPo> goodsSkuPos = goodsSkuPoMapper.selectByExample(goodsSkuPoExample);
        List<SimpleSkuVo> simpleSkuVos = new ArrayList<>();
        SimpleSkuVo simpleSkuVo = new SimpleSkuVo();
        for (GoodsSkuPo goodsSkuPo1 : goodsSkuPos) {
            simpleSkuVo.setId(goodsSkuPo1.getId());
            simpleSkuVo.setName(goodsSkuPo1.getName());
            simpleSkuVo.setInventory(goodsSkuPo1.getInventory());
            simpleSkuVo.setOriginalPrice(goodsSkuPo1.getOriginalPrice());
            simpleSkuVo.setImageUrl(goodsSkuPo1.getImageUrl());
            simpleSkuVo.setSkuSn(goodsSkuPo1.getSkuSn());
            Long price = getPrice(goodsSkuPo1.getId());
            if (price == null) {
                simpleSkuVo.setPrice(goodsSkuPo1.getOriginalPrice());
            } else {
                simpleSkuVo.setPrice(price);
            }
            simpleSkuVos.add(simpleSkuVo);
        }
        spuRetVo.setSkuList(simpleSkuVos);
        return spuRetVo;
    }

    /**
     * 新建sku
     *
     * @param spuId
     * @param shopId
     * @param skuCreatVo
     * @return GoodsSkuPo
     * @author zhai
     */
    public SkuOutputVo creatSku(Long spuId, Long shopId, SkuCreatVo skuCreatVo) {
        GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(spuId);
        //商家只能增加自家商品spu中的，shopId=0可以修改任意商品信息
        if (goodsSpuPo == null || goodsSpuPo.getDisabled() != 0 || !goodsSpuPo.getShopId().equals(shopId)) {
            return null;
        }
        GoodsSku goodsSku = new GoodsSku();
        GoodsSkuPo po = goodsSku.createPo(skuCreatVo, spuId);
        int ret = goodsSkuPoMapper.insertSelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("spuId :" + spuId + " 插入sku失败");
        } else {
            logger.info("spuId = " + spuId + " 插入sku成功");
        }
        SkuOutputVo skuOutputVo = new SkuOutputVo(po);
        return skuOutputVo;
    }

    /**
     * 管理员新增品牌
     *
     * @param brandInputVo
     * @return
     */
    public ReturnObject<Object> addBrand(BrandInputVo brandInputVo) {
        if (brandInputVo.getName() == null) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "品牌名称不能为空");
        }
        String name = brandInputVo.getName();
        BrandPoExample brandPoExample = new BrandPoExample();
        BrandPoExample.Criteria criteria = brandPoExample.createCriteria();
        criteria.andNameEqualTo(name);
        List<BrandPo> brandPos = brandPoMapper.selectByExample(brandPoExample);
        if (!brandPos.isEmpty()) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "品牌名称不能重复");
        }
        Brand brand = new Brand();
        BrandPo brandPo = brand.createAddPo(brandInputVo);
        int ret = brandPoMapper.insertSelective(brandPo);
        if (ret == 0) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } else {
            logger.info("品牌已新建成功");
            return new ReturnObject(new Brand(brandPo));
        }
    }
}
