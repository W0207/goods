package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PresaleActivityVo {
    @ApiModelProperty("预售活动id")
    private Long id;

    @ApiModelProperty("预售活动名字")
    private String name;

    @ApiModelProperty("预售活动开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("开始支付尾款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("预售活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("参与预售的商品spu列表")
    private List<SimpleSpuVo> simpleSpuVoList;

    @ApiModelProperty("参与预售的商店列表")
    private List<SimpleShopVo> simpleShopVoList;

    @ApiModelProperty("预售活动状态")
    private Integer state;

    @ApiModelProperty("活动库存量")
    private Integer quantity;

    @ApiModelProperty("定金")
    private Integer advancePayPrice;

    @ApiModelProperty("尾款")
    private Integer restPayPrice;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModified;
}
