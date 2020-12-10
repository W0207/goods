package cn.edu.xmu.flashsale.model.vo;

import cn.edu.xmu.flashsale.model.po.GoodsSkuPo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Data
@ApiOperation(value = "sku信息")
public class SimpleSkuVo {


    private Long id;

    private String name;

    private String skuSn;

    private  String imageUrl;

    private  Integer inventory;

    private Integer orginalPrice;

    private Integer price;


    private Boolean disable;

    public SimpleSkuVo(){}

    public SimpleSkuVo(GoodsSkuPo goodsSkuPo){

    }


}
