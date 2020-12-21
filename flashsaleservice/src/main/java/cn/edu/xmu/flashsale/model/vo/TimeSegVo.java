package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;
import cn.edu.xmu.external.bo.TimeSegInfo;
import java.time.LocalDateTime;
@Data
public class TimeSegVo {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public TimeSegVo(TimeSegInfo timeSegInfo){
        this.id=timeSegInfo.getId();
        this.beginTime=timeSegInfo.getBeginTime();
        this.endTime=timeSegInfo.getEndTime();
        this.gmtCreate=timeSegInfo.getGmtCreate();
        this.gmtModified=timeSegInfo.getGmtModified();
    }
}
