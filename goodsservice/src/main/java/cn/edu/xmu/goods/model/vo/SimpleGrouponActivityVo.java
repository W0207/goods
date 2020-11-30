package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleGrouponActivityVo {

    private Long id;

    private String name;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
