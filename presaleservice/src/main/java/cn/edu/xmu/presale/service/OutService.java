package cn.edu.xmu.presale.service;

import cn.edu.xmu.otherinterface.bo.MyReturn;
import cn.edu.xmu.otherinterface.service.OPresaleService;
import cn.edu.xmu.presale.dao.OutDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "0.0.1")
public class OutService implements OPresaleService {

    @Autowired
    OutDao outDao;

    @Override
    public MyReturn<Long> getAdvancePrice(Long presaleId) {
        return outDao.getAdvancePrice(presaleId);
    }

    @Override
    public MyReturn<Long> getRestPrice(Long presaleId) {
        return outDao.getRestPrice(presaleId);
    }
}
