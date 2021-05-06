package cn.edu.xmu.groupon.dao;

import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.ininterface.service.model.vo.SpuToGrouponVo;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import cn.edu.xmu.groupon.mapper.*;
import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.po.*;
import cn.edu.xmu.groupon.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhai
 */
@Repository
public class GrouponDao {

    private static final Logger logger = LoggerFactory.getLogger(GrouponDao.class);

    @Autowired
    GrouponActivityPoMapper grouponActivityPoMapper;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private InShopService inShopService;

    /**
     * 查看所有团购活动
     *
     * @param spuId
     * @param shopId
     * @param page
     * @param pageSize
     * @return ReturnObject<PageInfo < VoObject>>
     * @author zhai
     */
    public PageInfo<GrouponActivityPo> findgroupons(Integer timeLine, Long spuId, Long shopId, Integer page, Integer pageSize) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        if (spuId != null) {
            criteria.andGoodsSpuIdEqualTo(spuId);
        }
        if (shopId != null) {

            criteria.andShopIdEqualTo(shopId);
        }
        //时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的
        if (timeLine != null) {
            if (timeLine == 0) {
                //timeLine等于0还没开始的活动
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else {
                if (timeLine == 1) {
                    //timeLine等于1明天开始的活动
                    criteria.andBeginTimeGreaterThan(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth() + 1, 0, 0, 0));
                    criteria.andBeginTimeLessThan(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth() + 2, 0, 0, 0));

                } else {
                    if (timeLine == 2) {
                        //timeLine等于2正在进行的活动
                        criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                        criteria.andEndTimeGreaterThan(LocalDateTime.now());
                    } else {
                        //timeLine等于3已经结束的活动
                        if (timeLine == 3) {
                            criteria.andEndTimeLessThan(LocalDateTime.now());
                        }
                    }

                }
            }
        }
        List<GrouponActivityPo> grouponActivityPos;
        grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(grouponActivityPos);
    }

    /**
     * 查询shop团购活动
     *
     * @param state
     * @param spuId
     * @param id
     * @param page
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     * @author zhai
     */
    public ReturnObject<PageInfo<VoObject>> findShopGroupon(Integer state, Long spuId, Long id, Integer page, Integer pageSize, String beginTime, String endTime) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(id);
        if (state != null) {
            criteria.andStateEqualTo(state);
        }
        if (spuId != null) {
            criteria.andGoodsSpuIdEqualTo(spuId);
        }

        if (beginTime != null) {
            criteria.andBeginTimeEqualTo(beginTime);
        }
        if (endTime != null) {
            criteria.andEndTimeEqualTo(endTime);
        }
        PageHelper.startPage(page, pageSize);
        List<GrouponActivityPo> grouponActivityPos;
        grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
        List<VoObject> ret = new ArrayList<>(grouponActivityPos.size());
        for (GrouponActivityPo po : grouponActivityPos) {
            GrouponActivity grouponActivity = new GrouponActivity(po);
            ret.add(grouponActivity);
        }
        PageInfo<VoObject> grouponPage = PageInfo.of(ret);
        PageInfo<GrouponActivityPo> grouponPoPage = PageInfo.of(grouponActivityPos);
        PageInfo<VoObject> grouponAcvtivityPage = new PageInfo<>(ret);
        grouponAcvtivityPage.setPages(grouponPoPage.getPages());
        grouponAcvtivityPage.setPageNum(grouponPoPage.getPageNum());
        grouponAcvtivityPage.setPageSize(grouponPoPage.getPageSize());
        grouponAcvtivityPage.setTotal(grouponPoPage.getTotal());
        return new ReturnObject(grouponPage);
    }

    public PageInfo<GrouponActivityPo> findShopGroupon1(Integer state, Long spuId, Long id, Integer page, Integer pageSize, String beginTime, String endTime) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(id);
        if (state != null) {
            criteria.andStateEqualTo(state);
        }
        if (spuId != null) {
            criteria.andGoodsSpuIdEqualTo(spuId);
        }

        if (beginTime != null) {
            criteria.andBeginTimeEqualTo(beginTime);
        }
        if (endTime != null) {
            criteria.andEndTimeEqualTo(endTime);
        }
        List<GrouponActivityPo> grouponActivityPos;
        grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(grouponActivityPos);
    }

    /**
     * 新增团购活动
     *
     * @param id
     * @param grouponInputVo
     * @param shopId
     * @return
     * @author zhai
     */

    public ReturnObject addGroupon(Long id, GrouponInputVo grouponInputVo, Long shopId) {
        ReturnObject returnObject = null;
        boolean bool = inShopService.shopExitOrNot(shopId);
        if (!bool) {
            logger.info("该店铺不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
        }
        SpuToGrouponVo spuToGrouponVo = goodservice.grouponFindSpu(id);
        if (spuToGrouponVo == null) {
            logger.info("该商品不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("该商品不存在"));
        }
        ShopToAllVo shopToAllVo = inShopService.presaleFindShop(shopId);
        if (shopToAllVo == null) {
            logger.info("该店铺不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
        }
        if (!goodservice.spuInShopOrNot(shopId, id)) {
            logger.info("该商品不属于该店铺");
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该商品不属于该店铺"));
        }
        if (grouponInputVo.getBeginTime() == null) {

            return new ReturnObject(ResponseCode.Log_BEGIN_NULL);
        }
        if (grouponInputVo.getEndTime() == null) {
            return new ReturnObject(ResponseCode.Log_END_NULL);
        }
        if (grouponInputVo.getBeginTime().isAfter(grouponInputVo.getEndTime())) {
            logger.info("团购活动开始时间晚于结束时间");
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        if (grouponInputVo.getBeginTime().isBefore(LocalDateTime.now())) {
            logger.info("团购活动开始时间早于当前时间");
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, String.format("开始时间早于当前时间"));
        }
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);
//        List<GrouponActivityPo>grouponActivityPos=grouponActivityPoMapper.selectByExample(example);
//        if(grouponActivityPos!=null){
//            logger.info("该商品已参加团购活动");
//            return new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
//        }


        GrouponActivity grouponActivity = new GrouponActivity();
        GrouponActivityPo grouponActivityPo = grouponActivity.createAddPo(id, grouponInputVo, shopId);
        grouponActivityPo.setState(0);
        int ret = grouponActivityPoMapper.insertSelective(grouponActivityPo);
        if (ret == 0) {
            //检查新增是否成功
            logger.info("grouponId = " + id + " 的信息新增失败");
        } else {
            logger.info("grouponId = " + id + " 的信息已新增成功");
            GrouponActivity group = new GrouponActivity(grouponActivityPo);
            GrouponOutputVo grouponOutputVo = new GrouponOutputVo(grouponActivityPo);
            grouponOutputVo.setShopToAllVo(shopToAllVo);
            grouponOutputVo.setGoodsSpu(spuToGrouponVo);
            returnObject = new ReturnObject(grouponOutputVo);
        }

        return returnObject;
    }

    /**
     * 修改团购活动
     *
     * @param id             团购活动 id
     * @param grouponInputVo 可修改的团购活动信息
     * @return 返回对象 ReturnObject<Object>
     * @author zhai
     */
    public ReturnObject modifyGrouponById(Long id, GrouponInputVo grouponInputVo, Long shopId) {
        ReturnObject returnObject;
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        boolean bool = inShopService.shopExitOrNot(shopId);
        if (!bool) {
            logger.info("该店铺不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
        }
        if (grouponInputVo.getBeginTime().isAfter(grouponInputVo.getEndTime())) {
            logger.info("团购活动开始时间晚于结束时间");
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        if (grouponInputVo.getEndTime() == null || grouponInputVo.getBeginTime() == null) {
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        if (grouponInputVo.getBeginTime().isBefore(LocalDateTime.now())) {
            logger.info("团购活动开始时间早于当前时间时间");
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, String.format("团购活动开始时间早于当前时间时间"));
        }
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：Id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动不存在"));
        }
        Long shopid = po.getShopId() == null ? null : po.getShopId();
        if (shopId.equals(shopid)) {
            GrouponActivity grouponActivity = new GrouponActivity(po);
            GrouponActivityPo grouponActivityPo = grouponActivity.creatUpdatePo(grouponInputVo);
            if (po.getState() == 1) {
                logger.info("团购活动已上线，无法修改：grouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已上线"));
            }
            if (po.getState() == 2) {
                logger.info("团购活动已删除，无法修改：grouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已删除"));
            }
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(grouponActivityPo);
            if (ret == 0) {
                //检查更新是否成功
                logger.info("团购活动修改失败：grouponId = " + id);
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动修改失败"));
            } else {
                logger.info("grouponId = " + id + " 的信息已更新");
                returnObject = new ReturnObject();
                return returnObject;
            }

        } else {
            logger.error("无权限修改团购活动");
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);

        }
    }

    /**
     * 更改团购活动状态
     *
     * @param id 种类 id
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject deleteGrouponState(Long shopId, Long id) {

        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动不存在"));
        }
        boolean bool = inShopService.shopExitOrNot(shopId);
        if (!bool) {
            logger.info("该店铺不存在：shopId =" + shopId);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
        }

        if (po.getShopId() == null) {
            logger.info("无法修改：GrouponId = " + id);
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW, String.format("店铺不存在"));
        }

        if (po.getShopId().equals(shopId)) {
            if (po.getState() == 2) {
                logger.info("团购活动已删除：GrouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已删除"));
            } else if (po.getState() == 1) {
                logger.info("团购活动已上线，无法修改：GrouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已上线"));
            }

            po.setState(2);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动删除失败：GrouponId = " + id);
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动删除失败"));
            } else {
                logger.info("团购活动删除成功：GrouponId = " + id);
                returnObject = new ReturnObject();
                return returnObject;
            }
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("团购活动不属于该店铺"));
    }

    /**
     * 上线团购活动
     *
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject onGrouponState(Long shopId, Long id) {
        boolean bool = inShopService.shopExitOrNot(shopId);
        if (!bool) {
            logger.info("该店铺不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
        }
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动不存在"));
        }

        if (po.getShopId() == null) {
            logger.info("无法修改：GrouponId = " + id);
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW, String.format("店铺不存在"));
        }


        if (po.getShopId().equals(shopId)) {
            if (po.getState() == 2) {
                logger.info("团购活动已删除：GrouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已删除"));
            } else if (po.getState() == 1) {
                logger.info("团购活动已上线：GrouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已上线"));
            }
            po.setState(1);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动上线失败：GrouponId = " + id);
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购活动上线失败"));
            } else {
                logger.info("团购活动上线成功：GrouponId = " + id);
                returnObject = new ReturnObject();
                return returnObject;
            }
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("活动不属于该店铺"));
    }


    /**
     * 下线团购活动
     *
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject offGrouponState(Long shopId, Long id) {
        boolean bool = inShopService.shopExitOrNot(shopId);
        if (!bool) {
            logger.info("该店铺不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
        }
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("团购不存在"));
        }

        if (po.getShopId() == null) {
            logger.info("无法修改：GrouponId = " + id);
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW, String.format("店铺Id为空"));
        }


        if (po.getShopId().equals(shopId)) {
            if (po.getState() == 2) {
                logger.info("团购活动已删除：GrouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已删除"));
            } else if (po.getState() == 0) {
                logger.info("团购活动已下线：GrouponId = " + id);
                return new ReturnObject(ResponseCode.GROUPON_STATENOTALLOW, String.format("团购活动已下线"));
            }
            po.setState(0);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动下线失败：GrouponId = " + id);
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在"));
            } else {
                logger.info("团购活动下线成功：GrouponId = " + id);
                returnObject = new ReturnObject();
            }
            return returnObject;
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("活动不属于该店铺"));
    }

    public boolean disableActivity(Long shopId) {
        try {
            GrouponActivityPoExample example = new GrouponActivityPoExample();
            GrouponActivityPoExample.Criteria criteria = example.createCriteria();
            criteria.andShopIdEqualTo(shopId);
            List<Byte> states = new ArrayList<>();
            states.add((byte) 0);
            states.add((byte) 1);
            criteria.andStateIn(states);
            List<GrouponActivityPo> pos = grouponActivityPoMapper.selectByExample(example);
            for (GrouponActivityPo po : pos) {
                po.setGmtModified(LocalDateTime.now());
                po.setState(2);
                grouponActivityPoMapper.updateByPrimaryKey(po);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
