package cn.edu.xmu.flashsale.dao;

import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class OutDao {

    private static final Logger logger = LoggerFactory.getLogger(FlashSaleDao.class);

    public ReturnObject<Boolean> delFlashTimeSeg(Long segId) {
        return null;
    }
}
