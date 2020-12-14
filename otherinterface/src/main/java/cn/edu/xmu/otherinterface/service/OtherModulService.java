package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.TimeSegInfo;
import cn.edu.xmu.otherinterface.bo.UserInfo;

public interface OtherModulService {

    ReturnObject<TimeSegInfo> getTimeSegInfo(Long TimeSegId);

    ReturnObject<UserInfo> getUserInfo(Long userId);
}
