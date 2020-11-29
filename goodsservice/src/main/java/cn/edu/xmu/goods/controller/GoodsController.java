package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.vo.GoodsSpuStateVo;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查看商品spu的所有状态
     *
     * @return Object
     */
    @ApiOperation(value = "获得商品spu的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("spus/states")
    public Object getAllSpuStatus() {
        GoodsSpu.State[] states = GoodsSpu.State.class.getEnumConstants();
        List<GoodsSpuStateVo> spuStateVos = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            spuStateVos.add(new GoodsSpuStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(spuStateVos).getData());
    }


}
