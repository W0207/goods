package cn.edu.xmu.coupon.service;

import cn.edu.xmu.coupon.dao.CouponActivityDao;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.bo.CouponSku;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.vo.CouponActivityModifyVo;
import cn.edu.xmu.coupon.model.vo.CouponActivitySkuInputVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author BiuBiuBiu
 */
@Service
public class CouponActivityService {

    @Autowired
    CouponActivityDao couponActivityDao;

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityService.class);

    public ReturnObject deleteCouponSkuById(Long id, Long shopId) {
        return couponActivityDao.deleteCouponSkuById(id,shopId);
    }

    public ReturnObject modifyCouponActivityByID(Long id, Long shopId, CouponActivityModifyVo couponActivityModifyVo) {
        return couponActivityDao.modifyCouponActivityByID(id,shopId,couponActivityModifyVo);
    }

    public ReturnObject rangeForCouponActivityById(Long id, Long shopId, CouponActivitySkuInputVo couponActivitySkuInputVo) {
        ReturnObject returnObject;
        CouponSkuPo couponSkuPo=couponActivityDao.rangeForCouponActivityById(id,shopId,couponActivitySkuInputVo);
        if (couponSkuPo != null) {
            returnObject = new ReturnObject(new CouponSku(couponSkuPo));
            logger.debug("rangeForCouponActivityById : " + returnObject);
        } else {
            logger.debug("rangeForCouponActivityById : fail!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;
    }
}
