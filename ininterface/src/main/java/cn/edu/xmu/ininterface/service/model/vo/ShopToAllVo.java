package cn.edu.xmu.ininterface.service.model.vo;

import java.io.Serializable;

/**
 * @author 宇
 */
public class ShopToAllVo implements Serializable {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
