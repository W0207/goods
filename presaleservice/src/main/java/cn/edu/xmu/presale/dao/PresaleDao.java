package cn.edu.xmu.presale.dao;

import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.presale.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import cn.edu.xmu.presale.model.po.PresaleActivityPoExample;
import cn.edu.xmu.presale.model.vo.PresaleActivityRetVo;
import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PresaleDao {

    @DubboReference
    private InShopService inShopService;

    @DubboReference
    private Ingoodservice ingoodservice;

    @Autowired
    private PresaleActivityPoMapper presaleActivityPoMapper;

    public Long inserPresaleActivity(PresaleActivityPo presaleActivityPo)
    {
        int ret = presaleActivityPoMapper.insertSelective(presaleActivityPo);
        return (long)ret;
    }

    public ReturnObject<PageInfo<PresaleActivityRetVo>> findAllPresale(Long shopId, Integer timeLine, Long skuId, Integer page,Integer pageSize)
    {
        PresaleActivityPoExample presaleActivityPoExample = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = presaleActivityPoExample.createCriteria();
        PageHelper.startPage(page, pageSize);
        if(shopId!=null) {
            //shopId不为空
            criteria.andShopIdEqualTo(shopId);
        }
        //时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的
        if(timeLine!=null){
            if(timeLine==0){
                //timeLine等于0还没开始的活动
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else {
                if(timeLine==1){
                    //timeLine等于1明天开始的活动
                    criteria.andBeginTimeGreaterThan(LocalDateTime.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth(),LocalDateTime.now().getDayOfMonth()+1,0,0,0));
                } else {
                    if(timeLine==2){
                        //timeLine等于2正在进行的活动
                        criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                        criteria.andEndTimeGreaterThan(LocalDateTime.now());
                    } else {
                        //timeLine等于3已经结束的活动
                        if(timeLine == 3){
                            criteria.andEndTimeLessThan(LocalDateTime.now());
                        }
                    }

                }
            }
        }
        if(skuId!=null) {
            //spuId不为空
            criteria.andGoodsSkuIdEqualTo(skuId);
        }
        List<PresaleActivityPo> presaleActivityPos = presaleActivityPoMapper.selectByExample(presaleActivityPoExample);
        List<PresaleActivityRetVo> presaleActivityRetVos = new ArrayList<>(presaleActivityPos.size());
        for(PresaleActivityPo po: presaleActivityPos){
            PresaleActivityRetVo vo = new PresaleActivityRetVo(po);
            vo.setShopToAllVo(inShopService.presaleFindShop(po.getShopId()));
            presaleActivityRetVos.add(vo);
        }
        PageInfo<PresaleActivityPo> poPageInfo = new PageInfo<>(presaleActivityPos);
        PageInfo<PresaleActivityRetVo> voPageInfo = new PageInfo<>(presaleActivityRetVos);
        voPageInfo.setPages(poPageInfo.getPages());
        voPageInfo.setPageNum(poPageInfo.getPageNum());
        voPageInfo.setPageSize(poPageInfo.getPageSize());
        voPageInfo.setTotal(poPageInfo.getTotal());
        /**
         * 还缺少关于sku的信息
         */
        return new ReturnObject<>(voPageInfo);
    }

    public ReturnObject modifyPresale(Long shopId, Long id, PresaleActivityVo vo){
        ReturnObject returnObject = null;
        PresaleActivityPo presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        if(presaleActivityPo.equals(null)){
            //预售活动的id不存在
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("修改预售信息的时候，预售活动的id不存在"));
        }else {
            if (!shopId.equals(presaleActivityPo.getShopId())) {
                //预售活动的shopId与路径上的不同
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("修改预售信息的时候，预售活动的shopId与路径上的不同"));
            } else {
                if(presaleActivityPo.getState()== 1){
                    //预售活动已经关闭
                    returnObject = new ReturnObject(ResponseCode.PRESALE_STATENOTALLOW);
                } else {
                    //修改活动信息
                    presaleActivityPo.setName(vo.getName());
                    presaleActivityPo.setAdvancePayPrice(vo.getAdvancePayPrice());
                    presaleActivityPo.setRestPayPrice(vo.getRestPayPrice());
                    presaleActivityPo.setQuantity(vo.getQuantity());
                    presaleActivityPo.setBeginTime(vo.getBeginTime());
                    presaleActivityPo.setPayTime(vo.getPayTime());
                    presaleActivityPo.setEndTime(vo.getEndTime());
                    presaleActivityPo.setGmtModified(LocalDateTime.now());
                    int ret = presaleActivityPoMapper.updateByPrimaryKey(presaleActivityPo);
                    if (ret == 0) {
                        returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
                    } else {
                        returnObject = new ReturnObject();
                    }
                }
            }
        }
        return returnObject;
    }

    public ReturnObject deletePresale( Long shopId, Long id){
        ReturnObject returnObject= null;
        PresaleActivityPo presaleActivityPo= presaleActivityPoMapper.selectByPrimaryKey(id);
        if(presaleActivityPo.equals(null)){
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("删除预售活动时，预售活动的id不存在"));
        } else {
            if(!presaleActivityPo.getShopId().equals(shopId)){
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("删除预售活动时，商店的id与活动里是商店id不一致"));
            }

            else {

                presaleActivityPo.setState((byte) 1);
                presaleActivityPo.setGmtModified(LocalDateTime.now());
                int ret = presaleActivityPoMapper.updateByPrimaryKey(presaleActivityPo);
                if(ret==0)
                {
                    returnObject = new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
                } else {
                    returnObject = new ReturnObject();
                }
            }
        }
        return returnObject;
    }

    public ReturnObject queryPresaleofSKU( Long shopId,  Long id, Integer state){
        ReturnObject returnObject = null;
        PresaleActivityPoExample presaleActivityPoExample = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = presaleActivityPoExample.createCriteria();
        criteria.andGoodsSkuIdEqualTo(id);
        criteria.andShopIdEqualTo(shopId);
        if(!state.equals(null)){
            criteria.andStateEqualTo((byte) (int)state);
        }
        List<PresaleActivityPo> presaleActivityPos = presaleActivityPoMapper.selectByExample(presaleActivityPoExample);
        if(presaleActivityPos.equals(null)){
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,"没有对于shopID和skuID的优惠活动")；
        } else {
            List<PresaleActivityRetVo> presaleActivityRetVos = new ArrayList<>(presaleActivityPos.size());
            for(PresaleActivityPo po: presaleActivityPos){
                PresaleActivityRetVo vo = new PresaleActivityRetVo(po);
                vo.setShopToAllVo(inShopService.presaleFindShop(shopId));
                vo.setSpuToPresaleVo(ingoodservice.presaleFindSku(id));
                presaleActivityRetVos.add(vo);
            }
            return new ReturnObject(presaleActivityRetVos);
        }
        return returnObject;

    }
}
