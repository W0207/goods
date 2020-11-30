package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopVo {

    private Long id;

    private String name;

    private Integer state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
