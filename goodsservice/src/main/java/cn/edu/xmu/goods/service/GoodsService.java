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
import cn.edu.xmu.ininterface.service.model.vo.*;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.xmu.ininterface.service.Ingoodservice;

/**
 * @author Abin
 */
@Service
@DubboService(version = "0.0.1")
public class GoodsService implements Ingoodservice {

    @Autowired
    GoodsDao goodsDao;

    @Value("${goodsservice.dav.username}")
    private String davUsername;

    @Value("${goodsservice.dav.password}")
    private String davPassword;

    @Value("${goodsservice.dav.baseUrl}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public ReturnObject<PageInfo<VoObject>> findAllBrand(Integer page, Integer pageSize) {
        return goodsDao.findAllBrand(page, pageSize);
    }

    @Override
    public SkuToPresaleVo presaleFindSku(Long id) {
        GoodsSkuPo goodsSkuPo = goodsDao.findGoodsSkuById(id);
        if (goodsSkuPo == null || !goodsSkuPo.getDisabled().equals(0)) {
            return null;
        }
        SkuToPresaleVo skuToPresaleVo = new SkuToPresaleVo(goodsSkuPo.getId(), goodsSkuPo.getName(), goodsSkuPo.getSkuSn(), goodsSkuPo.getImageUrl(), goodsSkuPo.getInventory(), goodsSkuPo.getOriginalPrice(),
                goodsDao.getPrice(id) == null ? goodsSkuPo.getOriginalPrice() : goodsDao.getPrice(id), goodsSkuPo.getDisabled() == 0 ? false : true);
        return skuToPresaleVo;
    }

    @Override
    public SpuToGrouponVo grouponFindSpu(Long id) {
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(id);
        if (goodsSpuPo == null) {
            return null;
        }
        SpuGrouponVo spuGrouponVo = new SpuGrouponVo(goodsSpuPo);
        SpuToGrouponVo spuToGrouponVo = new SpuToGrouponVo();
        spuToGrouponVo.setId(spuGrouponVo.getId());
        spuToGrouponVo.setName(spuGrouponVo.getName());
        spuToGrouponVo.setGoodsSn(spuGrouponVo.getGoodsSn());
        spuToGrouponVo.setImageUrl(spuGrouponVo.getImageUrl());
        spuToGrouponVo.setGmtCreate(spuGrouponVo.getGmtCreate());
        spuToGrouponVo.setGmtModified(spuGrouponVo.getGmtModified());
        return spuToGrouponVo;
    }
    /**
     * @param id
     * @return
     */
    @Override
    public SkuToFlashSaleVo flashFindSku(Long id) {
        try {
            GoodsSkuPo goodsSkuPo = goodsDao.findGoodsSkuById(id);
            if (goodsSkuPo == null || !goodsSkuPo.getDisabled().equals(0)) {
                return null;
            }
            SkuToFlashSaleVo skuToFlashSaleVo = new SkuToFlashSaleVo(goodsSkuPo.getId(), goodsSkuPo.getName(), goodsSkuPo.getSkuSn(), goodsSkuPo.getImageUrl(), goodsSkuPo.getInventory(), goodsSkuPo.getOriginalPrice(),
                    goodsDao.getPrice(id) == null ? goodsSkuPo.getOriginalPrice() : goodsDao.getPrice(id), goodsSkuPo.getDisabled() == 0 ? false : true);
            return skuToFlashSaleVo;
        } catch (Exception e) {
            logger.error("findAllBrand: DataAccessException:" + e.getMessage());
            return null;
        }
    }

    /**
     * @param skuId
     * @return
     */
    @Override
    public boolean skuExitOrNot(Long skuId) {
        GoodsSkuPo po = goodsDao.findGoodsSkuById(skuId);
        if (!po.equals(null)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean skuInShopOrNot(Long shopId, Long id) {
        GoodsSkuPo goodsSkuPo = goodsDao.findGoodsSkuById(id);
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(goodsSkuPo.getGoodsSpuId());
        return shopId.equals(goodsSpuPo.getShopId());
    }


    @Override
    public SkuToCouponVo couponActivityFindSku(Long id)  {
        GoodsSkuPo goodsSkuPo = goodsDao.findGoodsSkuById(id);
        if (goodsSkuPo == null) {
            return null;
        }
        SkuCouponVo skuCouponVo = new SkuCouponVo(goodsSkuPo);

        SkuToCouponVo skuToCouponVo = new SkuToCouponVo();

        skuToCouponVo.setDisable(skuCouponVo.getDisable());
        skuToCouponVo.setGoodsSn(skuCouponVo.getGoodsSn());
        skuToCouponVo.setId(skuCouponVo.getId());
        skuToCouponVo.setImageUrl(skuCouponVo.getImageUrl());
        skuToCouponVo.setInventory(skuCouponVo.getInventory());
        skuToCouponVo.setOriginalPrice(skuCouponVo.getOriginalPrice());
        skuToCouponVo.setName(skuCouponVo.getName());
        return skuToCouponVo;
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
        if (brandPo != null) {
            returnObject = new ReturnObject(new Brand(brandPo));
            logger.debug("addBrand : " + returnObject);
        } else {
            logger.debug("addBrand : fail!");
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
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(spuId);
        if (goodsSpuPo == null) {
            logger.debug("修改spu信息中，spuId不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (goodsSpuPo.getDisabled() == 0) {
            logger.debug("修改spu信息中，禁止访问");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        ReturnObject returnObject;
        if (!shopId.equals(goodsSpuPo.getShopId())) {
            logger.debug("修改spu信息中，spu里shopId和路径上的shopId不一致");
            returnObject = new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        } else {
            goodsSpuPo.setId(spuId);
            goodsSpuPo.setGmtModified(LocalDateTime.now());
            goodsSpuPo.setSpec(spuInputVo.getSpecs());
            goodsSpuPo.setName(spuInputVo.getName());
            goodsSpuPo.setDetail(spuInputVo.getDescription());
            returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
        }
        return returnObject;
    }

    /**
     * sku上架
     *
     * @param shopId
     * @param skuId
     * @return
     */
    public ReturnObject putGoodsOnSaleById(Long shopId, Long skuId) {
        return goodsDao.putGoodsOnSaleById(shopId, skuId, 4L);
    }

    /**
     * sku下架
     *
     * @param skuId
     * @return ReturnObject
     */
    public ReturnObject putOffGoodsOnSaleById(Long shopId, Long skuId) {
        return goodsDao.putOffGoodsOnSaleById(shopId, skuId, 0L);
    }

    /**
     * sku删除
     *
     * @param shopId
     * @param skuId
     * @return ReturnObject
     */
    public ReturnObject deleteSkuById(Long shopId, Long skuId) {
        return goodsDao.deleteSkuById(shopId, skuId, 6L);
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

    public ReturnObject<PageInfo<VoObject>> findSkuSimple(Integer shopId, Integer page, Integer
            pageSize, Long spuId, String skuSn, String spuSn) {
        ReturnObject<PageInfo<VoObject>> returnObject = goodsDao.findSkuSimple(shopId, page, pageSize, spuId, skuSn, spuSn);
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
     * @author shangzhao zhai
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
     * spu加入品牌
     *
     * @param shopId
     * @param spuId
     * @param id
     * @return
     */
    public ReturnObject spuAddBrand(Long shopId, Long spuId, Long id) {
        ReturnObject returnObject;
        GoodsSpuPo tempSpu = goodsDao.findGoodsSpuById(spuId);
        if (tempSpu == null || tempSpu.getDisabled() == 0) {
            logger.debug("findGoodsSkuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            if (tempSpu.getShopId().equals(shopId)) {
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

    /**
     * 上传sku照片
     *
     * @param shopId
     * @param id
     * @param multipartFile
     * @return ReturnObject
     * @author shibin zhan
     */
    @Transactional
    public ReturnObject uploadSkuImg(Long shopId, Long id, MultipartFile multipartFile) {
        ReturnObject<GoodsSku> goodsSkuReturnObject = goodsDao.getGoodsSkuById(id);
        if (goodsSkuReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST || goodsSkuReturnObject.getData().getDisabled()) {
            logger.debug("uploadSkuImg : failed");
            return goodsSkuReturnObject;
        }
        Long shopid = goodsDao.findGoodsSpuById(goodsDao.findGoodsSkuById(id).getGoodsSpuId()).getShopId();
        if (shopid.equals(shopId)) {
            if (goodsSkuReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
                return goodsSkuReturnObject;
            }
            GoodsSku goodsSku = goodsSkuReturnObject.getData();
            ReturnObject returnObject = new ReturnObject();
            try {
                returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);
                //文件上传错误
                if (returnObject.getCode() != ResponseCode.OK) {
                    logger.debug(returnObject.getErrmsg());
                    return returnObject;
                }
                String oldFilename = null;
                if (goodsSku.getImageUrl() != null) {
                    int baseUrlIndex = goodsSku.getImageUrl().lastIndexOf("/");
                    oldFilename = goodsSku.getImageUrl().substring(baseUrlIndex + 1);
                }
                goodsSku.setImageUrl(baseUrl + returnObject.getData().toString());
                ReturnObject updateReturnObject = goodsDao.updateGoodsSkuImgUrl(goodsSku);

                //数据库更新失败，需删除新增的图片
                if (updateReturnObject.getCode() == ResponseCode.FIELD_NOTVALID) {
                    ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                    return updateReturnObject;
                }

                //数据库更新成功需删除旧图片，未设置则不删除
                if (oldFilename != null) {
                    ImgHelper.deleteRemoteImg(oldFilename, davUsername, davPassword, baseUrl);
                }
            } catch (IOException e) {
                logger.debug("uploadImg: I/O Error:" + baseUrl);
                return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
            }
            return returnObject;
        } else {
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
    }

    /**
     * 上传spu照片
     *
     * @param shopId
     * @param id
     * @param multipartFile
     * @return ReturnObject
     * @author shibin zhan
     */
    @Transactional
    public ReturnObject uploadSpuImg(Long shopId, Long id, MultipartFile multipartFile) {
        ReturnObject<GoodsSpu> goodsSpuReturnObject = goodsDao.getGoodsSpuById(id);
        if (goodsSpuReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST || goodsSpuReturnObject.getData().getDisabled()) {
            logger.debug("uploadSpuImg : failed");
            return goodsSpuReturnObject;
        }
        Long shopid = goodsDao.findGoodsSpuById(id).getShopId();
        if (shopid.equals(shopId)) {
            if (goodsSpuReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
                return goodsSpuReturnObject;
            }
            GoodsSpu goodsSpu = goodsSpuReturnObject.getData();
            ReturnObject returnObject = new ReturnObject();
            try {
                returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);
                //文件上传错误
                if (returnObject.getCode() != ResponseCode.OK) {
                    logger.debug(returnObject.getErrmsg());
                    return returnObject;
                }
                String oldFilename = null;
                if (goodsSpu.getImageUrl() != null) {
                    int baseUrlIndex = goodsSpu.getImageUrl().lastIndexOf("/");
                    oldFilename = goodsSpu.getImageUrl().substring(baseUrlIndex + 1);
                }
                goodsSpu.setImageUrl(baseUrl + returnObject.getData().toString());
                ReturnObject updateReturnObject = goodsDao.updateGoodsSpuImgUrl(goodsSpu);

                //数据库更新失败，需删除新增的图片
                if (updateReturnObject.getCode() == ResponseCode.FIELD_NOTVALID) {
                    ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                    return updateReturnObject;
                }

                //数据库更新成功需删除旧图片，未设置则不删除
                if (oldFilename != null) {
                    ImgHelper.deleteRemoteImg(oldFilename, davUsername, davPassword, baseUrl);
                }
            } catch (IOException e) {
                logger.debug("uploadImg: I/O Error:" + baseUrl);
                return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
            }
            return returnObject;
        } else {
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
    }

    /**
     * 上传品牌照片
     *
     * @param shopId
     * @param id
     * @param multipartFile
     * @return ReturnObject
     */
    @Transactional
    public ReturnObject uploadBrandImg(Long shopId, Long id, MultipartFile multipartFile) {
        ReturnObject<Brand> brandReturnObject = goodsDao.getBrandById(id);
        if (shopId == 0) {
            if (brandReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
                return brandReturnObject;
            }
            Brand brand = brandReturnObject.getData();
            ReturnObject returnObject = new ReturnObject();
            try {
                returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);
                //文件上传错误
                if (returnObject.getCode() != ResponseCode.OK) {
                    logger.debug(returnObject.getErrmsg());
                    return returnObject;
                }
                String oldFilename = null;
                if (brand.getImageUrl() != null) {
                    int baseUrlIndex = brand.getImageUrl().lastIndexOf("/");
                    oldFilename = brand.getImageUrl().substring(baseUrlIndex + 1);
                }
                brand.setImageUrl(baseUrl + returnObject.getData().toString());
                ReturnObject updateReturnObject = goodsDao.updateBrandImgUrl(brand);

                //数据库更新失败，需删除新增的图片
                if (updateReturnObject.getCode() == ResponseCode.FIELD_NOTVALID) {
                    ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                    return updateReturnObject;
                }
                //数据库更新成功需删除旧图片，未设置则不删除
                if (oldFilename != null) {
                    ImgHelper.deleteRemoteImg(oldFilename, davUsername, davPassword, baseUrl);
                }
            } catch (IOException e) {
                logger.debug("uploadImg: I/O Error:" + baseUrl);
                return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
            }
            return returnObject;
        } else {
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
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
        ReturnObject returnObject;
        GoodsSpuPo goodsSpuPo = goodsDao.findGoodsSpuById(spuId);
        if (goodsSpuPo == null || goodsSpuPo.getDisabled() == 0) {
            logger.debug("findGoodsSPuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "SpuId不存在");
        } else {
            if (!shopId.equals(goodsSpuPo.getShopId())) {
                logger.debug("spu增加种类的时候，shopid不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spuAddCategories，shopid不一致");
            } else {
                GoodsCategoryPo goodsCategoryPo = goodsDao.getCategoryById(id);
                if (goodsCategoryPo == null) {
                    logger.debug("spu增加种类的时候，CategoriesId不存在");
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spuAddCategories，shopid不一致");
                } else {
                    //当spu没有种类的时候，直接添加种类
                    if (goodsSpuPo.getCategoryId() == null) {
                        goodsSpuPo.setGmtModified(LocalDateTime.now());
                        goodsSpuPo.setCategoryId(id);
                        returnObject = goodsDao.modifySpuBySpuPoId(goodsSpuPo);
                    }
                    //spu有种类了
                    else {
                        if (goodsCategoryPo.getPid() == null) {
                            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spuAddCategories，CategoriesId不是二级种类");
                        } else {
                            if (!goodsCategoryPo.getPid().equals(goodsSpuPo.getCategoryId())) {
                                //不是二级种类，不予添加
                                logger.debug("spu增加种类的时候，CategoriesId不是二级种类");
                                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spuAddCategories，CategoriesId不是二级种类");
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
        }
        return returnObject;
    }

    /**
     * spu删除品牌
     *
     * @param shopId
     * @param spuId
     * @param id
     * @return
     */
    public ReturnObject spuDeleteBrand(Long shopId, Long spuId, Long id) {
        ReturnObject returnObject;
        GoodsSpuPo tempSpu = goodsDao.findGoodsSpuById(spuId);
        if (tempSpu == null || tempSpu.getDisabled() == 0) {
            logger.debug("findGoodsSPuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "SpuId不存在");
        } else {
            if (tempSpu.getShopId().equals(shopId) && tempSpu.getBrandId().equals(id)) {
                tempSpu.setGmtModified(LocalDateTime.now());
                tempSpu.setBrandId(null);
                returnObject = goodsDao.modifySpuBySpuPo(tempSpu);
            } else {
                logger.debug("spuDeleteBrand shopId和这个spu的里的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spuDeleteBrand shopId和这个spu的里的shopId不一致");
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
        ReturnObject returnObject;
        GoodsSpuPo tempSpu = goodsDao.findGoodsSpuById(spuId);
        if (tempSpu == null || tempSpu.getDisabled() == 0) {
            logger.debug("findGoodsSPuById : Not Found!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "SpuId不存在");
        } else {
            if (tempSpu.getShopId().equals(shopId) && tempSpu.getCategoryId().equals(id)) {
                tempSpu.setGmtModified(LocalDateTime.now());
                tempSpu.setCategoryId(null);
                returnObject = goodsDao.modifySpuBySpuPo(tempSpu);
            } else {
                logger.debug("spuDeleteBrand shopId和这个spu的里的shopId不一致");
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "spuDeleteBrand shopId和这个spu的里的shopId不一致");
            }
        }
        return returnObject;
    }

    /**
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject deleteSpuById(Long shopId, Long id) {
        return goodsDao.deleteSpuById(shopId, id);
    }

    /**
     * 管理员新增商品价格浮动
     *
     * @param shopId
     * @param id
     * @param floatPriceInputVo
     * @param userId
     * @return
     * @author shibin zhan
     */
    public ReturnObject<Object> addFloatPrice(Long shopId, Long id, FloatPriceInputVo floatPriceInputVo, Long userId) {
        return goodsDao.addFloatPrice(shopId, id, floatPriceInputVo, userId);
    }

    /**
     * 店家或管理员添加商品spu
     *
     * @param shopId
     * @param spuInputVo
     * @return
     */
    public ReturnObject addSpu(Long shopId, SpuInputVo spuInputVo) {
        return goodsDao.addSpu(shopId, spuInputVo);
    }

    /**
     * 获得sku的详细信息
     *
     * @param id
     * @return
     */
    public ReturnObject getSku(Long id) {
        return goodsDao.getSku(id);
    }

    /**
     * 获得spu的详细信息
     *
     * @param id
     * @return
     */
    public ReturnObject getSpu(Long id) {
        return goodsDao.getSpu(id);
    }

    /**
     * 新增Sku
     *
     * @param id
     * @param shopId
     * @param skuCreatVo
     * @return ReturnObject<Object>
     * @author zhai
     */
    public ReturnObject<Object> creatSku(Long id, Long shopId, SkuCreatVo skuCreatVo) {
        ReturnObject returnObject;
        SkuOutputVo skuOutputVo = goodsDao.creatSku(id, shopId, skuCreatVo);
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
