package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.ShopAuditVo;
import cn.edu.xmu.goods.model.vo.ShopStateVo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.goods.service.ShopService;
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
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 宇
 */
@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shop", produces = "application/json;charset=UTF-8")
public class ShopController {
    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);



    @Autowired
    private ShopService shopService;

    private int getStatue(ReturnObject returnObject)
    {
        if(returnObject.getCode()==ResponseCode.RESOURCE_ID_OUTSCOPE)
        {
            return HttpStatus.FORBIDDEN.value();
        }
        if(returnObject.getCode()==ResponseCode.FIELD_NOTVALID||returnObject.getCode()==ResponseCode.Log_Bigger||returnObject.getCode()==ResponseCode.Log_BEGIN_NULL||returnObject.getCode()==ResponseCode.Log_END_NULL){
            return HttpStatus.BAD_REQUEST.value();
        }
        if(returnObject.getCode()==ResponseCode.COUPONACT_STATENOTALLOW){
            return HttpStatus.FORBIDDEN.value();
        }
        if(returnObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            return HttpStatus.NOT_FOUND.value();
        }
        return HttpStatus.OK.value();
    }

    /**
     * 添加商店
     *
     * @return Object
     */
    @ApiOperation(value = "申请商店信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "String", name = "name", value = "可修改的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 908, message = "店铺已经存在")

    })
    @Audit
    @RequestMapping(value = "/shops", method = RequestMethod.POST)
    public Object addShop(@Validated @RequestBody ShopVo vo, @Depart Long departId,HttpServletResponse response) {
        if (departId != -1) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.USER_HASSHOP));
        }
        Shop shop = vo.createShop();
        shop.setGmtCreate(LocalDateTime.now());
        ReturnObject returnObject = shopService.insertShop(shop);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 查询所有状态
     */
    @ApiOperation(value = "获得店铺的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/shops/states")
    public Object getShopState() {
        Shop.State[] states = Shop.State.class.getEnumConstants();
        List<ShopStateVo> stateVos = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            stateVos.add(new ShopStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(stateVos).getData());
    }

    /**
     * 修改店家信息
     * by 宇
     */
    @ApiOperation(value = "修改店家信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "店家id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ShopVo", name = "vo", value = "可修改的用户信息", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/shops/{id}")
//    @RequestMapping(value = "/shops/{id}", method = RequestMethod.PUT)
    public Object modifyShop(@PathVariable("id") Long id,@Depart Long departId,  @RequestBody ShopVo shopVo, HttpServletResponse response) {
        if(!departId.equals(id)){
            ReturnObject returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            response.setStatus(getStatue(returnObject));
            return Common.decorateReturnObject(returnObject);
        }
        Shop shop = shopVo.createShop();
        shop.setId(id);
        shop.setGmtModified(LocalDateTime.now());
        ReturnObject returnObject = shopService.updateShop(shop);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * by 宇
     *
     * @return
     */
    @ApiOperation(value = "上架店家")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "店家id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/shops/{id}/onshelves")
    public Object shopOnShelves(@PathVariable Long id,HttpServletResponse response) {
        Shop shop = new Shop();
        shop.setId(id);
        shop.setState(Shop.State.UP);
        shop.setGmtModified(LocalDateTime.now());
        ReturnObject returnObject = shopService.shopOnShelves(shop);
        System.out.println(returnObject);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * by 宇
     *
     * @return
     */
    @ApiOperation(value = "下架店家")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "店家id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping("/shops/{id}/offshelves")
    public Object shopOffShelves(@PathVariable Long id,HttpServletResponse response) {
        Shop shop = new Shop();
        shop.setId(id);
        shop.setState(Shop.State.DOWN);
        shop.setGmtModified(LocalDateTime.now());
        ReturnObject returnObject = shopService.shopOffShelves(shop);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 店家下架或删除
     *
     * @return
     */
    @ApiOperation(value = "关闭店家")
    @ApiImplicitParams({
            //@ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "店家id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @DeleteMapping("/shops/{id}")
    public Object deleteShop(@PathVariable Long id,HttpServletResponse response) {
        Shop shop = new Shop();
        shop.setId(id);
        shop.setState(Shop.State.CLOSE);
        shop.setGmtModified(LocalDateTime.now());
        ReturnObject returnObject = shopService.deleteShop(shop);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 管理员审核店铺
     *
     * @param id:新店 id
     *              by 菜鸡骞
     * @return Object
     */
    @ApiOperation(value = "管理员审核店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "新店 id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ShopAuditVo", name = "conclusion", value = "可修改的新店信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{shopId}/newshops/{id}/audit")
    public Object auditShop(@PathVariable Long id, @Validated @RequestBody ShopAuditVo shopAuditVo, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("auditShop : Id = " + id + " vo = " + shopAuditVo);
        }

        ReturnObject returnObj = shopService.auditShopByID(id, shopAuditVo);
        response.setStatus(getStatue(returnObj));
        return Common.decorateReturnObject(returnObj);
    }

}
