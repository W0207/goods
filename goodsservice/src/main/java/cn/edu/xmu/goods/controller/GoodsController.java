package cn.edu.xmu.goods.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 **/
@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);



    /***
     *获得商品spu的所有状态
     */
    @ApiOperation(value = "获得商品spu的所有状态")
    @ApiImplicitParams({})
    @ApiResponses({})
    @Audit
    @GetMapping("/spus/state")
    public void getSpuState() {
    }


}
