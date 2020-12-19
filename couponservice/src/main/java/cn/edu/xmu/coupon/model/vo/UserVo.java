package cn.edu.xmu.coupon.model.vo;

public class UserVo {
    Long id;
    String name;

    public UserVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserVo() {
    }

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
