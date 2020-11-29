package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.vo.SpuStateVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Object getAllSpuStatus() {
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
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "skuId", required = true)
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/skus/{id}")
    public Object getGoodsSkuBySkuId(@PathVariable Long id) {
        Object returnObject;
        ReturnObject sku = goodsService.findGoodsSkuById(id);
        logger.debug("findGoodsSkuById: " + sku.getData() + "code = " + sku.getCode());
        if (!sku.getCode().equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            returnObject = ResponseUtil.ok(sku.getData());
        } else {
            returnObject = Common.getNullRetObj(new ReturnObject<>(sku.getCode(), sku.getErrmsg()), httpServletResponse);
        }
        return returnObject;
    }
}
