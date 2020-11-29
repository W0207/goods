package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandVo {

    private Long id;

    private String name;

    private String imageUrl;

    private String detail;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
