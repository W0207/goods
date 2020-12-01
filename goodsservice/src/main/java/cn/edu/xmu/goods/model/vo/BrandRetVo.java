package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "品牌视图对象")
public class BrandRetVo {

    private Long id;

    private String name;

    private String imageUrl;

    private String detail;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


    public BrandRetVo(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.imageUrl = brand.getImageUrl();
        this.detail = brand.getDetail();
        this.gmtCreate = brand.getGmtCreate();
        this.gmtModified = brand.getGmtModified();
    }
}
