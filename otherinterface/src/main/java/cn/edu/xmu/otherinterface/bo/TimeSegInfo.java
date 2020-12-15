package cn.edu.xmu.otherinterface.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TimeSegInfo implements Serializable {
    private Long id;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreat;

    private LocalDateTime gmtModified;
}
