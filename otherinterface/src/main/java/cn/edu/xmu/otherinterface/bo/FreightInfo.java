package cn.edu.xmu.otherinterface.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FreightInfo implements Serializable {

    private Long id;

    private String name;

    private Byte type;

    private Integer unit;

    private Boolean isDefault;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
