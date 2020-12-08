package cn.edu.xmu.coupon.controller;

import cn.edu.xmu.coupon.model.bo.Coupon;
import cn.edu.xmu.coupon.model.vo.CouponStateVo;
import cn.edu.xmu.coupon.service.CouponService;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
