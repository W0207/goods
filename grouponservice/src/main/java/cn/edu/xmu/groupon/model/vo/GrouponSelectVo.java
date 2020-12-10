package cn.edu.xmu.groupon.model.vo;

import cn.edu.xmu.groupon.model.bo.GrouponActivity;
import cn.edu.xmu.groupon.model.po.GrouponActivityPo;
import cn.edu.xmu.ooad.model.VoObject;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiOperation(value = "可修改的团购活动信息")
public class GrouponSelectVo implements VoObject {

    @ApiModelProperty(value = "团购活动")
    private Long id;

    @ApiModelProperty(value = "团购活动名称")
    private String name;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    public GrouponSelectVo(){}

    public GrouponSelectVo (GrouponActivityPo grouponActivityPo){
        this.id=grouponActivityPo.getId()==null ? null : grouponActivityPo.getId();
        this.name=grouponActivityPo.getName() == null ? null : grouponActivityPo.getName();
        this.beginTime=grouponActivityPo.getBeginTime() ==null ? null :grouponActivityPo.getBeginTime();
        this.endTime=grouponActivityPo.getEndTime()==null ? null :grouponActivityPo.getEndTime();
    }

    public GrouponSelectVo(GrouponActivity grouponActivity)
    {
        this.id=grouponActivity.getId();
        this.name=grouponActivity.getName();
        this.beginTime=grouponActivity.getBeginTime();
        this.endTime=grouponActivity.getEndTime();
    }
    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

}