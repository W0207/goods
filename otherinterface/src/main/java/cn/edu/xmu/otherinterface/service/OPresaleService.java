package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.otherinterface.bo.MyReturn;

public interface OPresaleService {

    MyReturn<Long> getAdvancePrice(Long presaleId);
    MyReturn<Long> getRestPrice(Long presaleId);
}
