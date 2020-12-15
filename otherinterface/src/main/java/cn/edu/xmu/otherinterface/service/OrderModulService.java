package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.FreightInfo;
import cn.edu.xmu.otherinterface.bo.MyReturn;

public interface OrderModulService {

    /**
     * 获取运费信息
     */
    MyReturn<FreightInfo> getFreightInfo(Long freightid);

}
