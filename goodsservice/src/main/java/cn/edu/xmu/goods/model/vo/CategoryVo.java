package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryVo {

    private Long id;

    private Long pid;

    private String name;

    private Long parentId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
