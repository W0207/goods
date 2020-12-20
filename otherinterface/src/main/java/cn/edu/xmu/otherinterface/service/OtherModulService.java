package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.otherinterface.bo.MyReturn;
import cn.edu.xmu.otherinterface.bo.TimeSegInfo;
import cn.edu.xmu.otherinterface.bo.UserInfo;

public interface OtherModulService {

    MyReturn<TimeSegInfo> getTimeSegInfo(Long TimeSegId);

    MyReturn<UserInfo> getUserInfo(Long userId);
}
