package cn.edu.xmu.coupon.controller;

import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.bo.Coupon;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponActivityPoExample;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.po.CouponSkuPoExample;
import cn.edu.xmu.coupon.model.vo.*;
import cn.edu.xmu.coupon.service.CouponActivityService;
import cn.edu.xmu.coupon.service.CouponService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ininterface.service.model.vo.SkuToCouponVo;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 权限控制器
 *
 * @author BiuBiuBiu
 */
@Api(value = "优惠服务", tags = "coupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/coupon", produces = "application/json;charset=UTF-8")
public class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponSkuPoMapper couponSkuPoMapper;

    @Autowired
    private CouponActivityPoMapper couponActivityPoMapper;

    @Autowired
    private CouponActivityService couponActivityService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    private int getStatue(ReturnObject returnObject) {
        if (returnObject.getCode() == ResponseCode.RESOURCE_ID_OUTSCOPE) {
            return HttpStatus.FORBIDDEN.value();
        }
        if (returnObject.getCode() == ResponseCode.FIELD_NOTVALID || returnObject.getCode() == ResponseCode.Log_Bigger || returnObject.getCode() == ResponseCode.Log_BEGIN_NULL || returnObject.getCode() == ResponseCode.Log_END_NULL) {
            return HttpStatus.BAD_REQUEST.value();
        }
        if (returnObject.getCode() == ResponseCode.COUPONACT_STATENOTALLOW) {
            return HttpStatus.FORBIDDEN.value();
        }
        if (returnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
            return HttpStatus.NOT_FOUND.value();
        }
        return HttpStatus.OK.value();
    }

    /**
     * 获得优惠券的所有状态
     *
     * @return Object
     * by 菜鸡骞
     */
    @ApiOperation(value = "获得优惠券的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/coupons/states")
    public Object getCouponState() {
        Coupon.State[] states = Coupon.State.class.getEnumConstants();
        List<CouponStateVo> couponStateVos = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            couponStateVos.add(new CouponStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(couponStateVos).getData());
    }

    /**
     * 查看上线的优惠活动列表
     *
     * @return Object
     * by 菜鸡骞
     */
    @ApiOperation(value = "查看上线的优惠活动列表")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/couponactivities")
    public Object showOwncouponactivities(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Long shopId, @RequestParam(required = false) Long timeline, HttpServletResponse response) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + "   shopId =" + shopId + "    timeline =" + timeline);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page <= 0 || pageSize <= 0) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "页数或页大小必须大于0");
        }
        ReturnObject<PageInfo<VoObject>> returnObject = couponService.showCouponactivities(page, pageSize, shopId, timeline);
        response.setStatus(getStatue(returnObject));
        return Common.getPageRetObject(returnObject);
    }


    /**
     * 查看本店下线的优惠活动列表
     * by 菜鸡骞
     *
     * @return Object
     */
    @ApiOperation(value = "查看本店下线的优惠活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/shops/{id}/couponactivities/invalid")
    public Object showOwnInvalidcouponacitvities(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @PathVariable(required = true) Long id, @Depart Long ShopId, HttpServletResponse response) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + " userid=" + id);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page <= 0 || pageSize <= 0) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "页数或页大小必须大于0");
        }
        if (!ShopId.equals(id)&&ShopId!=0) {
            ReturnObject returnObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            response.setStatus(getStatue(returnObj));
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        } else {
            ReturnObject<PageInfo<VoObject>> returnObject = couponService.showOwnInvalidcouponacitvitiesByid(page, pageSize, id);
            response.setStatus(getStatue(returnObject));
            return Common.getPageRetObject(returnObject);
        }
    }


    /**
     * 管理员修改己方某优惠活动
     *
     * @param id:活动id by 菜鸡骞
     * @return Object
     */
    @ApiOperation(value = "管理员修改己方某优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CouponActivityModifyVo", name = "优惠活动信息", value = "可修改的优惠活动信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{shopid}/couponactivities/{id}")
    public Object modifyCouponActivity(@PathVariable Long shopid, @PathVariable Long id, @Validated @RequestBody CouponActivityModifyVo couponActivityModifyVo, BindingResult bindingResult, @Depart Long shopId, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyCouponActivity : shopId = " + shopId + " vo = " + couponActivityModifyVo + "id = " + id);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while modifyCouponActivity : shopId = " + shopId + " vo = " + couponActivityModifyVo + "id = " + id);
            return returnObject;
        }
        ReturnObject returnObj = couponActivityService.modifyCouponActivityByID(id, shopid, couponActivityModifyVo,shopId);
        response.setStatus(getStatue(returnObj));
        return Common.decorateReturnObject(returnObj);
    }

    /**
     * 买家使用自己某优惠券
     *
     * @param id:优惠券 id
     *               <p>
     *               by 菜鸡骞
     * @return Object
     */
    @ApiOperation(value = "买家使用自己某优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/{id}")
    public Object useCoupon(@PathVariable Long id, @LoginUser Long userId, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("userCoupon : userId = " + userId + " couponId = " + id);
        }
        ReturnObject returnObj = couponService.useCouponByCouponId(id, userId);
        response.setStatus(getStatue(returnObj));
        return Common.decorateReturnObject(returnObj);
    }


    /**
     * 管理员为己方某优惠券活动新增限定范围
     *
     * @param id:活动 id
     *              by 菜鸡骞
     * @return Object
     */
    @ApiOperation(value = "管理员为己方某优惠券活动新增限定范围")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "优惠券活动id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "brandInputVo", name = "brandInputVo", value = "品牌详细信息", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 705, message = "无权限访问")
    })
    @Audit
    @PostMapping("/shops/{shopId}/couponactivities/{id}/skus")
    public Object rangeForCouponActivity(@PathVariable Long id, @Depart Long shopId, @Validated @RequestBody Long[] skuIds, HttpServletResponse response, BindingResult bindingResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("rangeForCouponActivity: activityId = " + id);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("rangeForCouponActivity: activityId = " + id);
            return returnObject;
        }
        ReturnObject couponActivity = couponActivityService.rangeForCouponActivityById(id, shopId, skuIds);
        response.setStatus(getStatue(couponActivity));
        returnObject = Common.decorateReturnObject(couponActivity);
        return returnObject;
//        return Common.decorateReturnObject(new ReturnObject());
    }

    /**
     * 管理员新建己方优惠活动
     * by YU
     *
     * @param shopId
     * @param vo
     * @return
     */
    @ApiOperation(value = "管理员新建己方优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "shopId", required = true)
    })
    @Audit // 需要认证
    @PostMapping("/shops/{shopId}/couponactivities")
    public Object addCouponActivity(@PathVariable Long shopId, @RequestBody AddCouponActivityVo vo, @LoginUser Long userId, HttpServletResponse response, @Depart Long ShopId) {
        if (!ShopId.equals(shopId)&&ShopId!=0) {
            ReturnObject returnObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            response.setStatus(getStatue(returnObj));
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        }
        CouponActivityPoExample couponActivityPoExample = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = couponActivityPoExample.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andStateEqualTo((byte) 1);
        //重复创建
        if (couponActivityPoMapper.selectByExample(couponActivityPoExample).size() != 0) {
            ReturnObject returnObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            response.setStatus(getStatue(returnObj));
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        }
        ReturnObject returnObject = couponService.addCouponActivity(shopId, userId, vo);
        response.setStatus(getStatue(returnObject));
        if (returnObject.getCode() == ResponseCode.OK) {
            response.setStatus(201);
        }
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 店家删除己方某优惠活动的某限定范围
     *
     * @param id
     * @param shopId
     * @return
     */
    @ApiOperation(value = "店家删除己方某优惠活动的某限定范围")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "Id", value = "skuId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @DeleteMapping("/shops/{shopId}/couponskus/{id}")
    public Object deleteCouponSku(@PathVariable Long shopId, @PathVariable Long id, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteCoupon : shopId = " + shopId + " skuId = " + id);
        }
        ReturnObject returnObj = couponActivityService.deleteCouponSkuById(id, shopId);
        return Common.decorateReturnObject(returnObj);
    }

    /**
     * 查看优惠活动中的商品
     *
     * @return Object
     * by 菜鸡骞
     */
    @ApiOperation(value = "查看优惠活动中的商品")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/couponactivities/{id}/skus")
    public Object viewGoodsInCoupon(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @PathVariable Long id, HttpServletResponse response) {

        CouponSkuPoExample couponSkuPoExample = new CouponSkuPoExample();
        CouponSkuPoExample.Criteria criteria = couponSkuPoExample.createCriteria();
        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if (couponActivityPo == null || couponActivityPo.getState() != 1) {
            ReturnObject returnObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            response.setStatus(getStatue(returnObj));
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        List<CouponSkuPo> couponSkuPos = new ArrayList<CouponSkuPo>();
        List<SkuToCouponVo> skuToCouponVos = new ArrayList<SkuToCouponVo>();
        criteria.andActivityIdEqualTo(id);
        couponSkuPos = couponSkuPoMapper.selectByExample(couponSkuPoExample);
        for (CouponSkuPo po : couponSkuPos) {
            SkuToCouponVo vo = goodservice.couponActivityFindSku(po.getSkuId());
            if (vo == null || vo.getDisable() != 0) {
            } else {
                skuToCouponVos.add(vo);
            }
        }
        logger.debug("showCoupons: page = " + page + "  pageSize =" + pageSize + "   activity_id =" + id);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page <= 0 || pageSize <= 0) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "页数或页大小必须大于0");
        }
        ReturnObject<PageInfo<VoObject>> returnObject = couponService.viewGoodsInCouponById(page, pageSize, skuToCouponVos);
        response.setStatus(getStatue(returnObject));
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 上传优惠活动照片
     *
     * @author shibin zhan
     */
    @ApiOperation(value = "上传优惠活动照片")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "Id", value = "skuId", required = true),
            @ApiImplicitParam(paramType = "formData", dataType = "file", name = "img", value = "文件", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 506, message = "该目录文件夹没有写入的权限"),
            @ApiResponse(code = 508, message = "图片格式不正确"),
            @ApiResponse(code = 509, message = "图片大小超限")
    })
    @Audit
    @PostMapping("/shops/{shopId}/couponactivities/{id}/uploadImg")
    public Object uploadCouponActivityImage(@PathVariable Long id, @PathVariable Long shopId, @RequestParam("img") MultipartFile multipartFile) {
        logger.debug("uploadCouponActivityImage: shopId = " + shopId + " spuId = " + id + " img :" + multipartFile.getOriginalFilename());
        ReturnObject returnObject = couponActivityService.uploadCouponActivityImg(shopId, id, multipartFile);
        return Common.getNullRetObj(returnObject, httpServletResponse);
    }

    /**
     * by YU
     *
     * @param shopId
     * @param id
     * @return
     */
    @ApiOperation("上线优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true)
    })
    @Audit
    @PutMapping("/shops/{shopId}/couponactivities/{id}/onshelves")
    public Object CouponActivityOnShelves(@PathVariable Long shopId, @PathVariable Long id, HttpServletResponse response) {
        ReturnObject returnObject = couponActivityService.CouponActivityOnShelves(shopId, id);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * by YU
     *
     * @param shopId
     * @param id
     * @return
     */
    @ApiOperation("下线优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true)
    })
    @Audit
    @PutMapping("/shops/{shopId}/couponactivities/{id}/offshelves")
    public Object CouponActivityOffShelves(@PathVariable Long shopId, @PathVariable Long id, HttpServletResponse response) {
        ReturnObject returnObject = couponActivityService.CouponActivityOffShelves(shopId, id);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * by 宇
     *
     * @param shopId
     * @param id
     * @return
     */
    @ApiOperation("管理员删除己方优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true)
    })
    @Audit
    @DeleteMapping("/shops/{shopId}/couponactivities/{id}")
    public Object deleteCouponActivity(@PathVariable Long shopId, @PathVariable Long id, HttpServletResponse response, @Depart Long ShopId) {
        if (!ShopId.equals(shopId)&&ShopId!=0) {
            ReturnObject returnObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            response.setStatus(getStatue(returnObj));
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE));
        }
        ReturnObject returnObject = couponActivityService.deleteCouponActivity(shopId, id);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 买家领取优惠券
     *
     * @param userId
     * @return
     * @author 宇
     */
    @ApiOperation("买家领取活动优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true)
    })
    @Audit
    @PostMapping("/couponactivities/{id}/usercoupons")
    public Object userGetCoupon(@PathVariable Long id, @LoginUser Long userId, HttpServletResponse response) {
        ReturnObject returnObject = couponService.userGetCoupon(id, userId);
        response.setStatus(getStatue(returnObject));
        if(returnObject.getCode()==ResponseCode.OK){
            response.setStatus(HttpStatus.CREATED.value());
        }
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 买家查看优惠券列表
     * by 菜鸡骞
     *
     * @return Object
     */
    @ApiOperation(value = "买家查看优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "优惠券状态", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/coupons")
    public Object showCoupons(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer state, @LoginUser Long userId, HttpServletResponse response) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + " userid=" + userId);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page <= 0 || pageSize <= 0 || (state != null && state != 0 && state != 1 && state != 2 && state != 3)) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "传入参数错误");
        }
        Object object = null;
        ReturnObject<PageInfo<VoObject>> returnObject = couponService.showCouponsById(page, pageSize, state, userId);
        response.setStatus(getStatue(returnObject));
        System.out.println(returnObject.getData());
        object = Common.getPageRetObject(returnObject);
        return object;
    }

    @ApiOperation(value = "查看优惠活动详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId", value = "shopId", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "活动id", required = true)
    })
    @Audit
    @GetMapping("/shops/{shopId}/couponactivities/{id}")
    public Object findCouponActivity(@PathVariable Long shopId, @PathVariable Long id, HttpServletResponse response) {
        ReturnObject returnObject = couponActivityService.findCouponActivity(shopId, id);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);
    }
}
