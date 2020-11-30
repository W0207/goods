package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.GoodsSpuStateVo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shop", produces = "application/json;charset=UTF-8")
public class ShopController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShopService shopService;
    /**
     * 查看商品spu的所有状态
     *
     * @return Object
     */
    @ApiOperation(value = "申请商店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "name", value = "可修改的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 908, message = "店铺已经存在")

    })
    @PostMapping("/shops")
    public Object addAShop(@Validated @RequestBody ShopVo vo, BindingResult bindingResult) {
        Object retObject = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != retObject) {
            return retObject;
        }
        Shop shop = vo.createShop();
        shop.setGmtCreate(LocalDateTime.now());
        ReturnObject returnObject = shopService.insertShop(shop);
        if(returnObject.getData() != null){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            System.out.println(returnObject.getData());
            System.out.println(returnObject.toString());
            return Common.decorateReturnObject(returnObject);
        } else {
            return Common.getNullRetObj(new ReturnObject<>(returnObject.getCode(), returnObject.getErrmsg()), httpServletResponse);
        }

    }
}
