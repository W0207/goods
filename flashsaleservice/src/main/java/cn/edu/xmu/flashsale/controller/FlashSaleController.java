package cn.edu.xmu.flashsale.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.edu.xmu.flashsale.model.vo.*;
import cn.edu.xmu.flashsale.model.bo.*;
import cn.edu.xmu.flashsale.model.po.*;
import cn.edu.xmu.flashsale.service.FlashSaleService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import org.apache.dubbo.config.annotation.DubboReference;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import cn.edu.xmu.ininterface.service.model.vo.*;
import cn.edu.xmu.ininterface.service.*;

/**
 * 权限控制器
 **/
@Api(value = "秒杀服务", tags = "flashsale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/flashsale", produces = "application/json;charset=UTF-8")
public class FlashSaleController {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleController.class);

    @Autowired
    private FlashSaleService flashSaleService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    /**
     * 查询某一时段秒杀活动详情
     * @param id
     * @return
     * @author zhai
     */
    @ApiOperation(value = "查询某一时段秒杀活动详情")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "id", required = true, dataType = "Long", paramType = "path", value = "时间段id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @GetMapping("/timesegments/{id}/flashsales")
    public Object queryTopicsByTime(@PathVariable Long id) {

        List<FlashSaleOutputVo> flashSaleOutputVos = flashSaleService.findFlashSaleByTime(id);
        ReturnObject<List> returnObject=new ReturnObject<List>(flashSaleOutputVos);
        return Common.decorateReturnObject(returnObject);

    }


    /**
     * 查询当前时段秒杀活动详情
     * @param id
     * @return
     * @author zhai
     */
    @ApiOperation(value = "查询某一时段秒杀活动详情")
    @ApiImplicitParams({

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/flashsales/current")
    public Object getCurrentflash( Long id) {

        //需要将当前时间传到其他模块，并要求其他模块返回当前时段id
        List<FlashSaleOutputVo> flashSaleOutputVos = flashSaleService.findFlashSaleByTime(id);
        ReturnObject<List> returnObject=new ReturnObject<List>(flashSaleOutputVos);
        return Common.decorateReturnObject(returnObject);

    }
    /**
     * 管理员修改秒杀活动
     *
     * @param id
     * @param flashSaleInputVo
     * @return
     * @author zhai
     */
    @ApiOperation(value = "管理员修改秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Integer", paramType = "path", value = "店铺id"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "秒杀id"),
            @ApiImplicitParam(name = "flashSaleInputVo", value = "可修改的秒杀活动信息", required = true, dataType = "FlashSaleInputVo", paramType = "body"),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @PutMapping("/shops/{did}/flashsales/{id}")
    public Object updateflashsale(@PathVariable Long id, @Validated @RequestBody FlashSaleInputVo flashSaleInputVo, BindingResult bindingResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("updateflashsale: id = " + id);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while updateflashsale flashSaleID = " + id);
            return returnObject;
        }
        ReturnObject returnObj = flashSaleService.updateFlashSale(id, flashSaleInputVo);
        return Common.decorateReturnObject(returnObj);

    }

    /**
     * 平台管理员删除某个时段秒杀
     *
     * @param id
     * @return
     * @author zhai
     */
    @ApiOperation(value = "平台管理员删除某个时段秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Integer", paramType = "path", value = "店铺id"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "秒杀id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @DeleteMapping("/shops/{did}/flashsales/{id}")
    public Object deleteflashsale(@PathVariable Long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteFlashSale: id = " + id);
        }
        ReturnObject returnObj = flashSaleService.deleteFlashSale(id);
        return Common.decorateReturnObject(returnObj);

    }


    @ApiOperation(value = "平台管理员向秒杀活动添加商品SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Integer", paramType = "path", value = "店铺id"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "秒杀活动id"),
            @ApiImplicitParam(name = "skuInputVo", value = "向秒杀活动添加的SKU信息", required = true, dataType = "SkuInputVo", paramType = "body"),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @PostMapping("/shops/{did}/flashsales/{id}/flashitems")
    public Object addSKUofTopic(@PathVariable Long id, @Validated @RequestBody SkuInputVo skuInputVo, BindingResult bindingResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("addSKUofTopic id = " + id);
        }
        // 校验前端数据
        Object ret = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (ret != null) {
            logger.info("incorrect data received while addSKUofTopic flashSaleID = " + id);
            return ret;
        }
        ReturnObject returnObject =null;
        SkuToFlashSaleVo skuToFlashSaleVo=goodservice.flashFindSku(skuInputVo.getSkuId());
        if(skuToFlashSaleVo==null){
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("skuID不存在"));
        }
        else {
            FlashSaleItem flashSaleItem = flashSaleService.addFlashSaleItem(id, skuInputVo);
            if (flashSaleItem==null){
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("sku添加失败"));
            }else {
                FlashSaleOutputVo flashSaleOutputVo=new FlashSaleOutputVo(flashSaleItem,skuToFlashSaleVo);
                returnObject = new ReturnObject<>(flashSaleOutputVo);
            }
        }
        return Common.decorateReturnObject(returnObject);

    }

    /**
     * 平台管理员在秒杀活动删除商品SKU
     *
     * @param id
     * @param fid
     * @return
     * @author zhai
     */
    @ApiOperation(value = "平台管理员在秒杀活动删除商品SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Integer", paramType = "path", value = "店铺id"),
            @ApiImplicitParam(name = "fid", required = true, dataType = "Integer", paramType = "path", value = "秒杀活动id"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "秒杀活动项Id")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @DeleteMapping("/shops/{did}/flashsales/{fid}/flashitems/{id}")
    public Object deleteFlashSaleSku(@PathVariable Long id, @PathVariable Long fid) {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteFlashSaleSku: id = " + id);
        }
        ReturnObject returnObj = flashSaleService.deleteFlashSaleSku(fid, id);
        return Common.decorateReturnObject(returnObj);

    }

    @ApiOperation(value = "平台管理员在某个时间段下新建秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "did", required = true, dataType = "Integer", paramType = "path", value = "店铺id"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "秒杀时间段"),
            @ApiImplicitParam(name = "flashInputVo", required = true, dataType = "FlashInputVo", paramType = "body", value = "添加的秒杀信息")
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{id}/flashsales")
    public Object createFlash(@PathVariable Long did, @PathVariable Long id, @Validated @RequestBody FlashSaleInputVo flashSaleInputVo) {
        if (logger.isDebugEnabled()) {
            logger.debug("createFlashSale : id = " + id);
        }
        if (did == 0) {
            ReturnObject returnObject = flashSaleService.createFlash(id, flashSaleInputVo);
            return Common.decorateReturnObject(returnObject);
        } else {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
    }
}
