package cn.edu.xmu.coupon.service;

import cn.edu.xmu.coupon.dao.CouponDao;
import cn.edu.xmu.coupon.model.vo.AddCouponActivityVo;
import cn.edu.xmu.coupon.model.vo.CouponAddLimitVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToCouponVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author BiuBiuBiu
 */
@Service
public class CouponService {

    @Autowired
    private CouponDao couponDao;

    public ReturnObject<PageInfo<VoObject>> showCouponactivities(Integer page, Integer pageSize, Long shopId, Long timeline) {
        return couponDao.showCouponactivities(page, pageSize, shopId, timeline);
    }

    public ReturnObject<PageInfo<VoObject>> showOwnInvalidcouponacitvitiesByid(Integer page, Integer pageSize, Long id) {
        return couponDao.showOwnInvalidcouponacitvitiesByid(page, pageSize, id);
    }

    public ReturnObject useCouponByCouponId(Long id, Long userId) {
        return couponDao.useCouponByCouponId(id, userId);
    }

    public ReturnObject addCouponActivity(Long shopId, AddCouponActivityVo addCouponActivityVo) {
        return couponDao.addCouponActivity(shopId, addCouponActivityVo);
    }

    public ReturnObject userGetCoupon(Long id,Long userId) {
        return couponDao.userGetCoupon(id, userId);
    }

    public ReturnObject<PageInfo<VoObject>> viewGoodsInCouponById(Integer page, Integer pageSize, List<SkuToCouponVo> skuToCouponVos) {
        return couponDao.viewGoodsInCouponById(page,pageSize,skuToCouponVos);
    }


}
