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
        logger.debug("findGoodsSpuById : spuId=" + id);
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
     * @param shopId
     * @param skuId
     * @return ReturnObject
     * @author shibin zhan
     */
    public ReturnObject<Object> deleteGoodsSku(Long shopId, Long skuId) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null) {
            logger.info("skuId = " + skuId + "不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (goodsSkuPo.getDisabled() != 4) {
            logger.info("skuId = " + skuId + "禁止访问");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = findGoodsSpuById(goodsSkuPo.getGoodsSpuId()).getShopId();
        if ((shopId == 0 || shopid.equals(shopId))) {
            GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
            GoodsSkuPo po = goodsSku.createDeleteStatePo();
            goodsSkuPoMapper.updateByPrimaryKeySelective(po);
            logger.info("skuId = " + skuId + "已删除");
            return new ReturnObject<>();
        } else {
            logger.info("非法访问");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
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
     * @param shopId
     * @param skuId
     * @param skuInputVo
     * @return
     */
    public ReturnObject<Object> modifySkuById(Long shopId, Long skuId, SkuInputVo skuInputVo) {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(skuId);
        if (goodsSkuPo == null || goodsSkuPo.getDisabled() != 4) {
            logger.info("skuId = " + skuId + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = findGoodsSpuById(goodsSkuPo.getGoodsSpuId()).getShopId();
        if (shopId == 0 || shopid.equals(shopId)) {
            GoodsSku goodsSku = new GoodsSku(goodsSkuPo);
            GoodsSkuPo po = goodsSku.createUpdatePo(skuInputVo);
            goodsSkuPoMapper.updateByPrimaryKeySelective(po);
            logger.info("skuId = " + skuId + " 的信息已更新");
            return new ReturnObject<>();
        } else
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
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
        if (floatPricePo.getValid() == 1) {
            logger.info("商品价格浮动已失效");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = findGoodsSpuById(findGoodsSkuById(floatPricePo.getGoodsSkuId()).getGoodsSpuId()).getShopId();
        ReturnObject<Object> returnObject;
        if (shopId == 0 || shopid.equals(shopId)) {
            FloatPrice floatPrice = new FloatPrice(floatPricePo);
            FloatPricePo po = floatPrice.createUpdateStatePo(loginUserId);
            floatPricePoMapper.updateByPrimaryKeySelective(po);
            logger.debug("invalidFloatPriceById : shopId=" + shopId + "floatPriceId=" + id + "invalidBy" + loginUserId);

            returnObject = new ReturnObject<>();
            return returnObject;
        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
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


    //修改部分信息
    public ReturnObject modifySpuBySpuPoId(GoodsSpuPo goodsSpuPo) {
        ReturnObject returnObject = null;
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

    //修改全部信息，针对有些值要设置为null的时候修改部分信息不能将参数传进去修改
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

    public GoodsCategoryPo getCategoryByid(Long id) {
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
        List<BrandPo> brandPos = null;
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
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        if (brandPo == null) {
            logger.info("品牌不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        ReturnObject<Object> returnObject;
        int ret = brandPoMapper.deleteByPrimaryKey(id);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("品牌不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            returnObject = new ReturnObject<>();
        }
        return returnObject;
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

    /**
     * 管理员新增品牌
     *
     * @param brandInputVo 品牌详细信息
     * @return 返回对象 ReturnObject<Object>
     */
    public BrandPo addBrandById(BrandInputVo brandInputVo) {
        Brand brand = new Brand();
        BrandPo brandPo = brand.createAddPo(brandInputVo);
        int ret = brandPoMapper.insertSelective(brandPo);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            //检查新增是否成功
            brandPo = null;
        } else {
            logger.info("品牌已新建成功");
        }
        return brandPo;
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
}
