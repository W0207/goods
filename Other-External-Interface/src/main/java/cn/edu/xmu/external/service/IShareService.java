package cn.edu.xmu.external.service;

import cn.edu.xmu.external.model.MyReturn;

public interface IShareService {

    MyReturn<Long> updateBeShare(Long customerId, Long goodsSkuId, Long sharerId);

    MyReturn<Boolean> verifyShare(Long shareId);

    void startGiveRebate();
}
