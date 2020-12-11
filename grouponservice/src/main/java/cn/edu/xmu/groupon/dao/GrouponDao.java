package cn.edu.xmu.groupon.dao;

import org.springframework.stereotype.Repository;
import cn.edu.xmu.groupon.mapper.*;
import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.po.*;
import cn.edu.xmu.groupon.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GrouponDao {

    private static final Logger logger = LoggerFactory.getLogger(GrouponDao.class);

    @Autowired
    GrouponActivityPoMapper grouponActivityPoMapper;

    /**
     * 查看所有团购活动
     *
     * @param timeline
     * @param spuId
     * @param shopId
     * @param page
     * @param pageSize
     * @return ReturnObject<PageInfo < VoObject>>
     * @Author zhai
     */
    public PageInfo<GrouponActivityPo> findgroupons(Integer timeline, Long spuId, Long shopId, Integer page, Integer pageSize) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        if (spuId != null) {
            if (!spuId.toString().isBlank()) {
                criteria.andGoodsSpuIdEqualTo(spuId);
            }
        }
        if (shopId != null) {
            if (!shopId.toString().isBlank()) {
                criteria.andShopIdEqualTo(shopId);
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
            if (!state.toString().isBlank()) {
                criteria.andStateEqualTo(state);
            }
        }
        if (spuId != null) {
            if (!spuId.toString().isBlank()) {
                criteria.andGoodsSpuIdEqualTo(spuId);
            }
        }

        if (beginTime != null) {
            if (!beginTime.isBlank()) {
                criteria.andBeginTimeEqualTo(beginTime);
            }
        }
        if (endTime != null) {
            if (!endTime.isBlank()) {
                criteria.andEndTimeEqualTo(endTime);
            }
        }
        PageHelper.startPage(page, pageSize);
        List<GrouponActivityPo> grouponActivityPos;
        try {
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
            return new ReturnObject<>(grouponPage);
        } catch (DataAccessException e) {
            logger.error("findgroupons: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 查询sku团购活动
     *
     * @param id
     * @param state
     * @param shopId
     * @return
     * @author zhai
     */
    public ReturnObject<List> findSkuGrouponById(Long id, Integer state, Long shopId) {
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);
        criteria.andShopIdEqualTo(shopId);
        List<GrouponActivityPo> grouponActivityPos = grouponActivityPoMapper.selectByExample(example);
        List<GrouponSelectVo> grouponSelectVos = new ArrayList<>(grouponActivityPos.size());
        if (grouponActivityPos == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        for (GrouponActivityPo po : grouponActivityPos) {
            grouponSelectVos.add(new GrouponSelectVo(po));
        }
        return new ReturnObject<>(grouponSelectVos);

    }

    /**
     * 新增团购活动  待完善
     *
     * @param id
     * @param grouponInputVo
     * @param shopId
     * @return
     * @Author zhai
     */

    public GrouponActivityPo addGroupon(Long id, GrouponInputVo grouponInputVo, Long shopId) {

        GrouponActivity grouponActivity = new GrouponActivity();
        GrouponActivityPo grouponActivityPo = grouponActivity.createAddPo(id, grouponInputVo, shopId);
        System.out.println(grouponActivityPo.getStrategy().toString());
        int ret = grouponActivityPoMapper.insertSelective(grouponActivityPo);
        if (ret == 0) {
            //检查新增是否成功
            grouponActivityPo = null;
        } else {
            logger.info("categoryId = " + id + " 的信息已新增成功");
        }
        logger.debug(grouponActivityPo.getStrategy().toString());
        return grouponActivityPo;
    }

    /**
     * 修改团购活动
     *
     * @param id             团购活动 id
     * @param grouponInputVo 可修改的团购活动信息
     * @return 返回对象 ReturnObject<Object>
     * @author zhai
     */
    public ReturnObject<Object> modifyGrouponById(Long id, GrouponInputVo grouponInputVo, Long shopId) {
        ReturnObject<Object> returnObject;
        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("团购活动不存在或已被删除：Id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Long shopid = po.getShopId() == null ? null : po.getShopId();
        if (shopId.equals(shopid) || shopId == 0) {
            GrouponActivity grouponActivity = new GrouponActivity(po);
            GrouponActivityPo grouponActivityPo = grouponActivity.creatUpdatePo(grouponInputVo);
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(grouponActivityPo);
            if (ret == 0) {
                //检查更新是否成功
                logger.info("团购活动修改失败：grouponId = " + id);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("grouponId = " + id + " 的信息已更新");
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        } else {
            logger.error("无权限修改团购活动");
            returnObject = new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
            return returnObject;
        }
    }

    /**
     * 更改团购活动状态
     *
     * @param id 种类 id
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> updateGrouponState(Long shopId, Long id) {

        GrouponActivityPo po = grouponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("团购活动不存在或已被删除：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        /**
         * if( po.getState()!=0){
         logger.info("团购活动已结束：GrouponId = " + id);
         return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
         }
         */
        if (po.getShopId().equals(shopId) || shopId == 0) {

            po.setState(6);
            System.out.println(po.getState());
            int ret = grouponActivityPoMapper.updateByPrimaryKeySelective(po);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("团购活动删除：GrouponId = " + id);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
    }

}
