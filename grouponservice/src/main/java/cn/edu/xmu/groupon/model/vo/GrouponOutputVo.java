package cn.edu.xmu.groupon.model.vo;
import cn.edu.xmu.ininterface.service.model.vo.*;
import cn.edu.xmu.groupon.model.po.GrouponActivityPo;

import java.time.LocalDateTime;


public class GrouponOutputVo {

    private Long id;

    private String name;

    private GoodsSpu goodsSpu;

    private ShopToAllVo shop;

    private String strategy;

    private Integer state;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public GrouponOutputVo(GrouponActivityPo grouponActivityPo){
        if(grouponActivityPo==null){

        }else {
            this.id = grouponActivityPo.getId() == null ? null : grouponActivityPo.getId();
            this.name = grouponActivityPo.getName() == null ? null : grouponActivityPo.getName();
            this.strategy = grouponActivityPo.getStrategy() == null ? null : grouponActivityPo.getStrategy();
            this.beginTime = grouponActivityPo.getBeginTime() == null ? null : grouponActivityPo.getBeginTime();
            this.endTime = grouponActivityPo.getEndTime() == null ? null : grouponActivityPo.getEndTime();
            this.gmtCreate = grouponActivityPo.getGmtCreate() == null ? null : grouponActivityPo.getGmtCreate();
            this.gmtModified = grouponActivityPo.getGmtModified() == null ? null : grouponActivityPo.getGmtModified();
            this.state=grouponActivityPo.getState()==null ? null :grouponActivityPo.getState();
        }
    }

    public void setGoodsSpu(SpuToGrouponVo goodsSpu) {
        this.goodsSpu=new GoodsSpu();
        this.goodsSpu.setId(goodsSpu.getId());
        this.goodsSpu.setName(goodsSpu.getName());
        this.goodsSpu.setImageUrl(goodsSpu.getImageUrl());
        this.goodsSpu.setGmtModified(goodsSpu.getGmtModified());
        this.goodsSpu.setGoodsSn(goodsSpu.getGoodsSn());
        this.goodsSpu.setDisable(goodsSpu.getDisable());
        this.goodsSpu.setGmtCreat(goodsSpu.getGmtCreate());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShopToAllVo getShopToAllVo() {
        return shop;
    }

    public void setShopToAllVo(ShopToAllVo shop) {
        this.shop = shop;
    }

    public GoodsSpu getSimpleSpuVo() {
        return goodsSpu;
    }


    public Integer getState() {
        return state;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public String getName() {
        return name;
    }

    public String getStrategy(){
        return strategy;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public GrouponOutputVo (){}
}
