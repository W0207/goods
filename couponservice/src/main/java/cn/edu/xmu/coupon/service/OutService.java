package cn.edu.xmu.coupon.service;

import cn.edu.xmu.coupon.dao.OutDao;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.CouponActivity;
import cn.edu.xmu.otherinterface.bo.MyReturn;
import cn.edu.xmu.otherinterface.service.CouponModelService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(version = "0.0.1")
public class OutService implements CouponModelService {

    @Autowired
    OutDao outDao;

    @Override
    public MyReturn<List<CouponActivity>> getCouponActivity(Long goodsSkuId) {
        return outDao.getCouponActivity(goodsSkuId);
    }
}
