package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleSpuVo {

    private Long id;

    private String name;

    private String goodsSn;

    private String imageUrl;

    private Integer state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Boolean disabled;

}
