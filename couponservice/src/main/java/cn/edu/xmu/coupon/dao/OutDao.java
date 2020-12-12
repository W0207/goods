package cn.edu.xmu.coupon.dao;

import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.po.CouponSkuPoExample;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.CouponActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OutDao {

    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    @Autowired
    CouponSkuPoMapper couponSkuPoMapper;

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;

    public ReturnObject<List<CouponActivity>> getCouponActivity(Long goodsSkuId) {
        try {
            CouponSkuPoExample example = new CouponSkuPoExample();
            CouponSkuPoExample.Criteria criteria = example.createCriteria();
            criteria.andSkuIdEqualTo(goodsSkuId);
            List<CouponSkuPo> pos = couponSkuPoMapper.selectByExample(example);
            //如果为空的话，也返回正确，但是为空
            //ids存放活动的id
            List<Long> ids = new ArrayList<>();
            for(CouponSkuPo po: pos){
                if(!ids.contains(po.getActivityId())){
                    ids.add(po.getActivityId());
                }
            }
            List<CouponActivity> activities = new ArrayList<>(ids.size());
            for(Long id :ids){
                CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
                CouponActivity couponActivity = new CouponActivity(couponActivityPo.getId(),couponActivityPo.getName(),couponActivityPo.getBeginTime(),couponActivityPo.getEndTime());
                activities.add(couponActivity);
            }
            return new ReturnObject<>(activities);
        } catch (Exception e){
         return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误%s",e.getMessage()));
        }
    }
}
