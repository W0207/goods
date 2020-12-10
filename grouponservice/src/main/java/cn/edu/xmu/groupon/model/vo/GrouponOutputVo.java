package cn.edu.xmu.groupon.model.vo;

import cn.edu.xmu.groupon.model.bo.GrouponActivity;
import cn.edu.xmu.groupon.model.po.GrouponActivityPo;
import cn.edu.xmu.groupon.model.vo.*;

import java.time.LocalDateTime;


public class GrouponOutputVo {
    private Long id;

    private String name;

    private SimpleSpuVo goodsSpu;

    private SimpleShopVo shop;

    private String strategy;

    private Integer state;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public GrouponOutputVo(SimpleShopVo simpleShopVo, SimpleSpuVo simpleSpuVo, GrouponActivityPo grouponActivityPo){
        this.id=grouponActivityPo.getId()==null ? null :grouponActivityPo.getId();
        this.name=grouponActivityPo.getName()==null ? null :grouponActivityPo.getName();
        this.goodsSpu.setId(simpleSpuVo.getId()==null ? null :simpleSpuVo.getId());
        this.goodsSpu.setName(simpleSpuVo.getName()==null ? null :simpleSpuVo.getName());
        this.goodsSpu.setImageUrl(simpleSpuVo.getImageUrl()==null ? null :simpleSpuVo.getImageUrl());
        this.goodsSpu.setGmtCreat(simpleSpuVo.getGmtCreat()==null ? null : simpleSpuVo.getGmtCreat());
        this.goodsSpu.setGmtModified(simpleSpuVo.getGmtModified()==null ? null : simpleSpuVo.getGmtCreat());
        this.goodsSpu.setDisable(simpleSpuVo.getDisable());
        this.shop.setId(simpleShopVo.getId()==null ? null :simpleShopVo.getId());
        this.shop.setName(simpleShopVo.getName()== null ? null :simpleShopVo.getName());
        this.strategy=grouponActivityPo.getStrategy()==null?null :grouponActivityPo.getStrategy();
        this.beginTime=grouponActivityPo.getBeginTime() == null ? null :grouponActivityPo.getBeginTime();
        this.endTime=grouponActivityPo.getEndTime() ==null ? null :grouponActivityPo.getEndTime();
        this.gmtCreate=LocalDateTime.now()==null ?null :grouponActivityPo.getGmtCreate();
        this.gmtModified=LocalDateTime.now()==null ?null :grouponActivityPo.getGmtModified();

    }
}
