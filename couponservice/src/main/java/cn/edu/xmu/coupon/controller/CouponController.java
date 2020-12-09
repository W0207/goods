package cn.edu.xmu.coupon.controller;

import cn.edu.xmu.coupon.model.bo.Coupon;
import cn.edu.xmu.coupon.model.vo.CouponStateVo;
import cn.edu.xmu.coupon.service.CouponService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 *
 * @author BiuBiuBiu*/
@Api(value = "优惠服务", tags = "coupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/coupon", produces = "application/json;charset=UTF-8")
public class CouponController {

    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    @Autowired
    private HttpServletResponse httpServletResponse;


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
    @GetMapping("/states")
    public Object getCouponState() {
        logger.debug("getCouponState");
        Coupon.State[] states = Coupon.State.class.getEnumConstants();
        List<CouponStateVo> couponStateVos = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            couponStateVos.add(new CouponStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(couponStateVos).getData());
    }

    /**
     * 查看上线的优惠活动列表
     * @return Object
     * by 菜鸡骞
     */
    @ApiOperation(value = "查看上线的优惠活动列表")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/couponactivities")
    public Object showOwncouponactivities(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @PathVariable(required = false) Long shopId,@PathVariable(required = false) Long timeline) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + "   shopId =" + shopId+ "    timeline =" + timeline);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 60 : pageSize;
        shopId = (shopId==null) ? null : shopId;
        timeline = (timeline==null) ? 2 : timeline;
        ReturnObject<PageInfo<VoObject>> returnObject = couponService.showCouponactivities(page, pageSize, shopId,timeline);
        return Common.getPageRetObject(returnObject);
    }


    /**
     * 查看本店下线的优惠活动列表
     *
     * by 菜鸡骞
     * @return Object
     */
    @ApiOperation(value = "查看本店下线的优惠活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit // 需要认证
    @GetMapping("/shops/{id}/couponactivities/invalid")
    public Object showOwnInvalidcouponacitvities(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,@Depart Long id) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + " userid=" + id);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 60 : pageSize;
        ReturnObject<PageInfo<VoObject>> returnObject = couponService.showOwnInvalidcouponacitvitiesByid(page, pageSize,id);
        return Common.getPageRetObject(returnObject);
    }

}
