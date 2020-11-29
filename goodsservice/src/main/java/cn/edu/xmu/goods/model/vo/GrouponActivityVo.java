package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import jdk.vm.ci.meta.Local;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GrouponActivityVo {

    @ApiModelProperty("团购活动id")
    private Long id;

    @ApiModelProperty("团购活动名字")
    private String name;

    @ApiModelProperty("参与团购的商品spu列表")
    private List<SimpleSpuVo> simpleSpuVoList;

    @ApiModelProperty("参与团购的商店列表")
    private List<SimpleShopVo> simpleShopVoList;

    @ApiModelProperty("团购规则JSON")
    private String strategy;

    @ApiModelProperty("团购活动状态")
    private Integer state;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModified;
}
