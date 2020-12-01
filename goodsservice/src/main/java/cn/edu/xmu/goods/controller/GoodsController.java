package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.vo.SkuInputVo;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.goods.model.vo.SpuStateVo;
import cn.edu.xmu.goods.service.BrandService;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 **/
@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 查看商品spu的所有状态
     *
     * @return Object
     */
    @ApiOperation(value = "获得商品spu的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/spus/states")
    public Object getGoodsSpuState() {
        logger.debug("getGoodsSpuState");
        GoodsSpu.State[] states = GoodsSpu.State.class.getEnumConstants();
        List<SpuStateVo> spuStateVos = new ArrayList<>();
        for (GoodsSpu.State state : states) {
            spuStateVos.add(new SpuStateVo(state));
        }
        return ResponseUtil.ok(new ReturnObject<List>(spuStateVos).getData());
    }

    /**
     * 获得sku的详细信息
     *
     * @param `id`
     * @return Object
     */
    @ApiOperation(value = "获得sku的详细信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "skuId", required = true)
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/skus/{id}")
    public Object getSku(@PathVariable Long id) {
        Object returnObject;
        ReturnObject sku = goodsService.findGoodsSkuById(id);
        logger.debug("getSku : skuId = " + id);
        if (!sku.getCode().equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            returnObject = ResponseUtil.ok(sku.getData());
        } else {
            returnObject = Common.getNullRetObj(new ReturnObject<>(sku.getCode(), sku.getErrmsg()), httpServletResponse);
        }
        return returnObject;
    }

    /**
     * 查看一条商品SPU的详细信息
     *
     * @param id
     * @return Object
     */
    @ApiOperation(value = "查看一条商品spu的详细信息")
    @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "spuId")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/spus/{id}")
    public Object getGoodsSpuBySpuId(@PathVariable Long id) {
        Object returnObject;
        ReturnObject spu = goodsService.findGoodsSpuById(id);
        logger.debug("findGoodsSpuById: " + spu.getData() + "code = " + spu.getCode());
        if (!spu.getCode().equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            returnObject = ResponseUtil.ok(spu.getData());
        } else {
            returnObject = Common.getNullRetObj(new ReturnObject<>(spu.getCode(), spu.getErrmsg()), httpServletResponse);
        }
        return returnObject;
    }


    /**
     * 店家修改商品spu
     *
     * @param bindingResult 校验信息
     * @param spuInputVo    修改信息的SpuInputVo
     * @return Object
     */
    @ApiOperation(value = "店家修改商品spu")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "spuId", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "SpuInputVo", name = "spuInputVo", value = "可修改的用户信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @PutMapping("shops/{shopId}/spus/{id}")
    public Object modifyGoodsSpu(@PathVariable Long shopId, @PathVariable Long id, @Validated @RequestBody SpuInputVo spuInputVo, BindingResult bindingResult, @Depart Long shopid) {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyGoodsSpu : shopId = " + shopId + " spuId = " + id + " vo = " + spuInputVo);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while modifyGoodsSpu shopId = " + shopId + " spuId = " + id);
            return returnObject;
        }
        //商家只能修改自家商品spu，shopId=0可以修改任意商品信息
        if (shopId == shopid || shopId == 0) {
            ReturnObject returnObj = goodsService.modifySpuInfo(id, spuInputVo);
            return Common.decorateReturnObject(returnObj);
        } else {
            logger.error("无权限修改本商品的信息");
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
    }

    /**
     * 店家逻辑删除商品spu
     *
     * @param shopId 店铺id
     * @param id     spuId
     * @return Object
     */
    @ApiOperation(value = "店家删除商品spu")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "spuId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @DeleteMapping("shops/{shopId}/spus/{id}")
    public Object deleteGoodsSpu(@PathVariable Long shopId, @PathVariable Long id, @Depart Long shopid) {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGoodsSpu : shopId = " + shopId + " spuId = " + id);
        }
        //商家只能逻辑删除自家商品spu，shopId=0可以逻辑删除任意商品spu
        if (shopId == shopid || shopId == 0) {
            ReturnObject returnObj = goodsService.deleteSpuById(id);
            return Common.decorateReturnObject(returnObj);
        } else {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
    }

    /**
     * 店家商品上架
     *
     * @param id
     * @param shopId
     * @return Object
     */
    @ApiOperation(value = "店家商品上架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "spuId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @PutMapping("/shops/{shopId}/spus/{id}/onshelves")
    public Object putGoodsOnSales(@PathVariable Long shopId, @PathVariable Long id, @Depart Long shopid) {
        if (logger.isDebugEnabled()) {
            logger.debug("putGoodsOnSales : shopId = " + shopId + " spuId = " + id);
        }
        //商家只能上架自家商品spu，shopId=0可以上架任意商品spu
        if (shopId == shopid || shopId == 0) {
            ReturnObject returnObj = goodsService.putGoodsOnSaleById(id);
            return Common.decorateReturnObject(returnObj);
        } else {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
    }

    /**
     * 店家商品下架
     *
     * @param id
     * @param shopId
     * @return Object
     */
    @ApiOperation(value = "店家商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "spuId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @PutMapping("/shops/{shopId}/spus/{id}/offshelves")
    public Object putOffGoodsOnSales(@PathVariable Long shopId, @PathVariable Long id, @Depart Long shopid) {
        if (logger.isDebugEnabled()) {
            logger.debug("putGoodsOnSales : shopId = " + shopId + " spuId = " + id);
        }
        //商家只能上架自家商品spu，shopId=0可以上架任意商品spu
        if (shopId.equals(shopid) || shopId == 0) {
            ReturnObject returnObj = goodsService.putOffGoodsOnSaleById(id);
            return Common.decorateReturnObject(returnObj);
        } else {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
    }

    /**
     * 管理员或店家逻辑删除sku
     *
     * @param shopId 店铺id
     * @param id     spuId
     * @return Object
     */
    @ApiOperation(value = "管理员或店家逻辑删除sku")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "skuId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @DeleteMapping("shops/{shopId}/skus/{id}")
    public Object deleteGoodsSku(@PathVariable Long shopId, @PathVariable Long id, @Depart Long shopid) {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGoodsSku : shopId = " + shopId + " skuId = " + id);
        }
        //商家只能逻辑删除自家商品sku，shopId=0可以逻辑删除任意商品sku
        if (shopId.equals(shopid) || shopId == 0) {
            ReturnObject returnObj = goodsService.deleteSkuById(id);
            return Common.decorateReturnObject(returnObj);
        } else {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }


    }

    /**
     * 查看所有品牌
     *
     * @return Object
     */
    @ApiOperation(value = "查看所有品牌")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/brands")
    public Object getAllBrand(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        logger.debug("getAllBrand: page = " + page + "  pageSize =" + pageSize);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 49 : pageSize;

        logger.debug("getAllBrand: page = " + page + "  pageSize =" + pageSize);
        ReturnObject<PageInfo<VoObject>> returnObject = brandService.findAllBrand(page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 管理员或店家修改商品sku
     *
     * @param bindingResult 校验信息
     * @param skuInputVo    修改信息的SkuInputVo
     * @return Object
     */
    @ApiOperation(value = "店家修改商品spu")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "spuId", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "SkuInputVo", name = "skuInputVo", value = "可修改的sku信息", required = true)
    })
    //@Audit //需要认证
    @PutMapping("shops/{shopId}/skus/{id}")
    public Object modifySku(@PathVariable Long shopId, @PathVariable Long id, @Validated @RequestBody SkuInputVo skuInputVo, BindingResult bindingResult, @Depart Long shopid) {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyGoodsSku : shopId = " + shopId + " skuId = " + id + " vo = " + skuInputVo);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while modifyGoodsSku shopId = " + shopId + " skuId = " + id);
            return returnObject;
        }
        //商家只能修改自家商品spu，shopId=0可以修改任意商品信息
        if (shopId == shopid || shopId == 0) {
            ReturnObject returnObj = goodsService.modifySkuInfo(id, skuInputVo);
            return Common.decorateReturnObject(returnObj);
        } else {
            logger.error("无权限修改本商品的信息");
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
        }
    }
}