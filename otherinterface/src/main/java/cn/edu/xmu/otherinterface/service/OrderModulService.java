package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.FreightInfo;

public interface OrderModulService {

    /**
     *获取运费信息
     */
    ReturnObject<FreightInfo> getFreightInfo(Long freightid);

}
