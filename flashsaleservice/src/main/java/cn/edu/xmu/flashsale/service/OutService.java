package cn.edu.xmu.flashsale.service;

import cn.edu.xmu.flashsale.dao.OutDao;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.service.FlashTimeSegService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService(version = "0.0.1")
public class OutService implements FlashTimeSegService {

    @Autowired
    OutDao outDao;

    @Override
    public ReturnObject<Boolean> delFlashTimeSeg(Long segId) {
        return outDao.delFlashTimeSeg(segId);
    }
}
