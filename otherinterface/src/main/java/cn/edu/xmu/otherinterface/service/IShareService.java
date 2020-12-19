package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.otherinterface.bo.MyReturn;

public interface IShareService {

    MyReturn<Long> updateBeShare(Long customerId, Long goodsSkuId, Long sharerId);

    MyReturn<Boolean> verifyShare(Long shareId);

    void startGiveRebate();
}
