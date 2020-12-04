package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.bo.GoodsCategory;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public ReturnObject<PageInfo<VoObject>> findAllBrand(Integer page, Integer pageSize) {
        return goodsDao.findAllBrand(page, pageSize);
    }

    /**
     * 管理员删除品牌
     *
     * @param id
     * @return
     */
    public ReturnObject deleteBrandById(Long id) {
        return goodsDao.deleteBrandById(id);
    }

    /**
     * 管理员修改品牌
     *
     * @param shopId
     * @param id
     * @param brandInputVo
     * @return
     */
    public ReturnObject modifyBrandInfo(Long shopId, Long id, BrandInputVo brandInputVo) {
        return goodsDao.modifyBrandById(shopId, id, brandInputVo);
    }

    /**
     * 管理员新增品牌
     *
     * @param brandInputVo 品牌详细信息
     * @return 返回对象 ReturnObject<Object>
     */
    public ReturnObject<Object> addBrand(BrandInputVo brandInputVo) {
        ReturnObject returnObject;
        BrandPo brandPo = goodsDao.addBrandById(brandInputVo);
        if (brandInputVo != null) {
            returnObject = new ReturnObject(new Brand(brandPo));
            logger.debug("addBrand : " + returnObject);
        } else {
            logger.debug("addBrand : fail!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;

    }

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
    public ReturnObject<Object> modifySpuInfo(Long shopId, Long spuId, SpuInputVo spuInputVo) {
        ReturnObject returnObject = null;
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(spuId);
        if (goodsSpuPo == null) {
            logger.debug("修改spu信息中，spuId不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("修改spu信息中，spuId不存在"));
        } else {
            if (shopId != goodsSpuPo.getShopId() && shopId != 0) {
                logger.debug("修改spu信息中，spu里shopId和路径上的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("修改spu信息中，spu里shopId和路径上的shopId不一致"));
            } else {
                goodsSpuPo.setId(spuId);
                goodsSpuPo.setGmtModified(LocalDateTime.now());
                goodsSpuPo.setSpec(spuInputVo.getSpecs());
                goodsSpuPo.setName(spuInputVo.getName());
                goodsSpuPo.setDetail(spuInputVo.getDescription());
                returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
            }
        }
        return returnObject;
    }

    /**
     * @param spuId
     * @return ReturnObject
     */
    public ReturnObject<Object> deleteSpuById(Long shopId, Long spuId) {
        ReturnObject returnObject = null;
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(spuId);
        if (goodsSpuPo == null) {
            //spuId不存在
            logger.debug("删除spu信息中，spuId不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("删除spu信息中，spuId不存在"));
        } else {
            if (shopId != goodsSpuPo.getShopId() && shopId != 0) {
                //shopId不对
                logger.debug("删除spu信息中，spu里shopId和路径上的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("删除spu信息中，spu里shopId和路径上的shopId不一致"));
            } else {
                //删除商品spu
                return goodsDao.updateGoodsSpuState(spuId, 6L);
            }
        }
        return returnObject;
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
     * @param shopId
     * @param skuId
     * @return ReturnObject
     */
    public ReturnObject deleteSkuById(Long shopId, Long skuId) {
        return goodsDao.deleteGoodsSku(shopId, skuId);
    }

    /**
     * @param shopId
     * @param id
     * @param skuInputVo
     * @return
     */
    public ReturnObject modifySkuInfo(Long shopId, Long id, SkuInputVo skuInputVo) {
        return goodsDao.modifySkuById(shopId, id, skuInputVo);
    }

    public ReturnObject<PageInfo<VoObject>> findSkuSimple(Integer shopId, String skuSn, Integer page, Integer pageSize, String spuId, String skuSn1, String spuSn) {
        ReturnObject<PageInfo<VoObject>> returnObject = goodsDao.findSkuSimple(shopId, skuSn, page, pageSize, spuId, skuSn1, spuSn);
        return returnObject;
    }

    /**
     * @param id
     * @return
     */
    public ReturnObject invalidFloatPriceById(Long shopId, Long id, Long loginUserId) {
        return goodsDao.invalidFloatPriceById(shopId, id, loginUserId);
    }

    /**
     * 管理员新增商品类目
     *
     * @param id              种类 id
     * @param categoryInputVo 类目详细信息
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> addCategory(Long id, CategoryInputVo categoryInputVo) {
        ReturnObject returnObject;
        GoodsCategoryPo goodsCategoryPo = goodsDao.addCategoryById(id, categoryInputVo);
        if (goodsCategoryPo != null) {
            returnObject = new ReturnObject(new GoodsCategory(goodsCategoryPo));
            logger.debug("addCategory : " + returnObject);
        } else {
            logger.debug("addCategory : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;
    }

    /**
     * 管理员修改商品类目信息
     *
     * @param id              种类 id
     * @param categoryInputVo 类目详细信息
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> modifyCategory(Long id, CategoryInputVo categoryInputVo) {
        return goodsDao.modifyCategoryById(id, categoryInputVo);
    }


    /**
     * 管理员删除商品类目信息
     *
     * @param id 种类 id
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> deleteCategoryById(Long id) {
        return goodsDao.deleteCategoryById(id);
    }

    /**
     * @param shopId
     * @param spuId
     * @param id
     * @return
     */
    public ReturnObject spuAddBrand(Long shopId, Long spuId, Long id) {
        ReturnObject returnObject = null;
        GoodsSpuPo tempSpu = goodsDao.findGoodsSpuById(spuId);
        if (tempSpu == null) {
            logger.debug("findGoodsSkuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            if (tempSpu.getShopId() == shopId) {
                BrandPo brandPo = goodsDao.findBrandById(id);
                if (brandPo != null) {
                    GoodsSpuPo goodsSpuPo = new GoodsSpuPo();
                    goodsSpuPo.setBrandId(id);
                    goodsSpuPo.setId(spuId);
                    goodsSpuPo.setGmtModified(LocalDateTime.now());
                    returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
                } else {
                    logger.debug("findBrandById : Not Found!");
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
                }
            } else {
                logger.debug("spuAddBrand shopId和这个spu的里的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }
        return returnObject;
    }

    /**
     * spu增加品牌
     *
     * @param shopId
     * @param spuId
     * @param id
     * @return
     */
    public ReturnObject spuDeleteBrand(Long shopId, Long spuId, Long id) {
        ReturnObject returnObject = null;
        GoodsSpuPo tempSpu = goodsDao.findGoodsSpuById(spuId);
        System.out.println(tempSpu.toString());
        if (tempSpu == null) {
            logger.debug("findGoodsSPuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("SpuId不存在"));
        } else {
            if (tempSpu.getShopId() == shopId) {
                tempSpu.setGmtModified(LocalDateTime.now());
                tempSpu.setBrandId(null);
                returnObject = goodsDao.modifySpuBySpuPo(tempSpu);
                tempSpu = goodsDao.findGoodsSpuById(spuId);
                System.out.println(tempSpu.toString());
            } else {
                logger.debug("spuDeleteBrand shopId和这个spu的里的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spuDeleteBrand shopId和这个spu的里的shopId不一致"));
            }
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
    public ReturnObject<List> selectCategories(Long id) {
        ReturnObject<List> ret = goodsDao.getCategoryByPid(id);
        return ret;
    }

    @Transactional
    public ReturnObject uploadSkuImg(Long shopId, Long id, MultipartFile multipartFile) {
        return null;
    }

    /**
     * @param shopId
     * @param id
     * @param multipartFile
     * @return
     */
    @Transactional
    public ReturnObject uploadSpuImg(Long shopId, Long id, MultipartFile multipartFile) {
        return null;
    }

    @Transactional
    public ReturnObject uploadBrandImg(Long shopId, Long id, MultipartFile multipartFile) {
        return null;
    }

    /**
     * spu增加种类
     *
     * @param shopId
     * @param spuId
     * @param id
     * @return by 宇
     */
    public ReturnObject spuAddCategories(Long shopId, Long spuId, Long id) {
        ReturnObject returnObject = null;
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(spuId);
        if (goodsSpuPo == null) {
            logger.debug("findGoodsSPuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("SpuId不存在"));
        } else {
            if (shopId != goodsSpuPo.getShopId()) {
                logger.debug("spu增加种类的时候，shopid不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spuAddCategories，shopid不一致"));
            } else {
                GoodsCategoryPo goodsCategoryPo = goodsDao.getCategoryByid(id);
                if (goodsCategoryPo == null) {
                    logger.debug("spu增加种类的时候，CategoriesId不存在");
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spuAddCategories，shopid不一致"));
                } else {
                    //当spu没有种类的时候，直接添加种类
                    if (goodsSpuPo.getCategoryId() == 0) {
                        goodsSpuPo.setGmtModified(LocalDateTime.now());
                        goodsSpuPo.setCategoryId(id);
                        returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
                    }
                    //spu有种类了
                    else {
                        if (goodsCategoryPo.getPid() == 0) {
                            //不是二级种类，不予添加
                            logger.debug("spu增加种类的时候，CategoriesId不是二级种类");
                            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spuAddCategories，CategoriesId不是二级种类"));
                        } else {
                            //是二级目录，更改数据库
                            goodsSpuPo.setGmtModified(LocalDateTime.now());
                            goodsSpuPo.setCategoryId(id);
                            returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
                        }
                    }
                }
            }
        }
        return returnObject;
    }

    /**
     * by 宇
     *
     * @param shopId
     * @param spuId
     * @param id
     * @return
     */
    public ReturnObject spuDeleteCategories(Long shopId, Long spuId, Long id) {
        ReturnObject returnObject = null;
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(spuId);
        if (goodsSpuPo == null) {
            logger.debug("findGoodsSPuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("SpuId不存在"));
        } else {
            if (shopId != goodsSpuPo.getShopId()) {
                logger.debug("spu删除种类的时候，shopid不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spuAddCategories，shopid不一致"));
            } else {
                if (id != goodsSpuPo.getCategoryId()) {
                    logger.debug("spu删除种类的时候，CategoriesId不一致");
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("spuAddCategories，CategoriesId不一致"));
                } else {
                    logger.info("spuDeleteCategories");
                    goodsSpuPo.setCategoryId(0L);
                    goodsSpuPo.setGmtModified(LocalDateTime.now());
                    returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
                }
            }
        }
        return returnObject;
    }

    /**
     * 新增Sku
     *
     * @param id
     * @param shopId
     * @param skuCreatVo
     * @return  ReturnObject<Object>
     * @author 翟尚召
     */
    public ReturnObject<Object> creatSku(Long id, Long shopId, SkuCreatVo skuCreatVo) {
        ReturnObject returnObject;
        SkuOutputVo skuOutputVo= goodsDao.creatSku(id,shopId,skuCreatVo);
        if (skuOutputVo != null) {
            returnObject = new ReturnObject(skuOutputVo);
            logger.debug("addSKU : " + returnObject);
        } else {
            logger.debug("addSKU : Failed!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;
    }
}
