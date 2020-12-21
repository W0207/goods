package cn.edu.xmu.external.service;
import java.time.LocalTime;
import cn.edu.xmu.external.bo.TimeSegInfo;
import cn.edu.xmu.external.model.MyReturn;
import cn.edu.xmu.ooad.util.ReturnObject;

import java.time.LocalDateTime;

public interface ITimeService {

    Long getCurrentSegId(LocalDateTime localDateTime);

    boolean isSegExist(Long segId);

    Byte getSegType(Long segId);

    MyReturn<TimeSegInfo> getTimeSeg(Long segId);

    MyReturn<Long> getCurrentSegIdPlusFs(LocalTime now);
}
