package cn.edu.xmu.coupon.dao;

import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.bo.CouponSku;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.vo.CouponActivityModifyVo;
import cn.edu.xmu.coupon.model.vo.CouponActivitySkuInputVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author BiuBiuBiu
 */

@Repository
public class CouponActivityDao implements InitializingBean{

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;

    @Autowired
    CouponSkuPoMapper couponSkuPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityDao.class);

    public ReturnObject<Object> modifyCouponActivityByID(Long id, Long shopId, CouponActivityModifyVo couponActivityModifyVo) {

        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if (couponActivityPo == null) {
            logger.info("优惠活动id= " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(!couponActivityPo.getShopId().equals(shopId)&&shopId!=0)
        {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        CouponActivity couponActivity = new CouponActivity(couponActivityPo);
        CouponActivityPo po = couponActivity.createModifyPo(couponActivityModifyVo);
        couponActivityPoMapper.updateByPrimaryKeySelective(po);
        logger.info("couponActivityId = " + id + " 的信息已更新");
        return new ReturnObject<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public CouponSkuPo rangeForCouponActivityById(Long id, Long shopId, CouponActivitySkuInputVo couponActivitySkuInputVo) {
        CouponSku couponSku = new CouponSku();
        CouponSkuPo couponSkuPo = couponSku.createAddCouponActivityPo(couponActivitySkuInputVo,id,shopId);

        int ret = couponSkuPoMapper.insertSelective(couponSkuPo);
        if (ret == 0) {
            //检查新增是否成功
            couponSkuPo = null;
        } else {
            logger.info("新增范围成功");
        }
        return couponSkuPo;
    }
}
