package cn.edu.xmu.presale.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleActivityRetVo {
    private String name;

    private Long advancePayPrice;

    private Long restPayPrice;

    private Integer quantity;

    private LocalDateTime beginTime;

    private LocalDateTime payTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private String state;

}
