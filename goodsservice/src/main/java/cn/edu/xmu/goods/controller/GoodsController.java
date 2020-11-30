package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.vo.SpuInputVo;
import cn.edu.xmu.goods.model.vo.SpuStateVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
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
    GoodsService goodsService;

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
        for (int i = 0; i < states.length; i++) {
            spuStateVos.add(new SpuStateVo(states[i]));
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
            @ApiImplicitParam(paramType = "body", dataType = "SkuInputVo", name = "spuInputVo", value = "可修改的用户信息", required = true),

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
     * @param shopId        店铺id
     * @param id            spuId
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
}