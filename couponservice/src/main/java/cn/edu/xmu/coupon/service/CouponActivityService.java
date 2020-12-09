package cn.edu.xmu.coupon.service;

import cn.edu.xmu.coupon.dao.CouponActivityDao;
import cn.edu.xmu.coupon.model.vo.CouponActivityModifyVo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author BiuBiuBiu
 */
@Service
public class CouponActivityService {

    @Autowired
    CouponActivityDao couponActivityDao;


    public ReturnObject modifyCouponActivityByID(Long id, Long shopId, CouponActivityModifyVo couponActivityModifyVo) {
        return couponActivityDao.modifyCouponActivityByID(id,shopId,couponActivityModifyVo);
    }
}
