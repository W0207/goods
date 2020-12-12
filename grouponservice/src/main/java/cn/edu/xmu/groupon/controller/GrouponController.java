package cn.edu.xmu.groupon.controller;

import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.po.*;
import cn.edu.xmu.groupon.model.vo.*;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.edu.xmu.groupon.service .GrouponServer;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import cn.edu.xmu.ininterface.service.model.vo.*;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import cn.edu.xmu.ininterface.service.*;

/**
 * 权限控制器
 **/
@Api(value = "团购服务", tags = "groupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/groupon", produces = "application/json;charset=UTF-8")
public class GrouponController {

    private static final Logger logger = LoggerFactory.getLogger(GrouponController.class);

    @Autowired
    private GrouponServer grouponServer;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private InShopService inShopService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    /**
     * 获得团购活动的所有状态
     *
     * @return Object
     * @Author zhai
     */
    @ApiOperation(value = "获得团购活动的所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/groupons/states")
    public Object getgrouponState() {
        logger.debug("getGoodsSpuState");
        GrouponActivity.State[] states = GrouponActivity.State.class.getEnumConstants();
        List<GrouponStateVo> grouponStateVos = new ArrayList<>();
        for (GrouponActivity.State state : states) {
            grouponStateVos.add(new GrouponStateVo(state));
        }
        return ResponseUtil.ok(new ReturnObject<List>(grouponStateVos).getData());
    }

    /**
     * 查询所有团购活动
     *
     * @return Object
     * @Author zhai
     */
    @ApiOperation(value = "查询所有团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spuid", value = "spuId", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "shopId", value = "商店id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @GetMapping("/groupons")
    public Object queryGroupons(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Integer timeline,
            @RequestParam(required = false) Long spuid
    ) {
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 60 : pageSize;
        ReturnObject<PageInfo<VoObject>> returnObject = grouponServer.findgrouponActivity(timeline, spuid, shopId, page, pageSize);
        logger.debug("findgrouponActivity = " + returnObject);
        return Common.getPageRetObject(returnObject);

    }


    /**
     * 管理员查询所有团购（包括下线,删除的）
     *
     * @return Object
     * @Author zhai
     **/
    @ApiOperation(value = "管理员查询所有团购（包括下线,删除的）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商店id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spuid", value = "spuId", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @GetMapping("/shops/{id}/groupons")
    public Object queryGroupon(@PathVariable Long id,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer pageSize,
                               @RequestParam(required = false) String beginTime,
                               @RequestParam(required = false) String endTime,
                               @RequestParam(required = false) Integer state,
                               @RequestParam(required = false) Long spuId
    ) {
        ReturnObject<PageInfo<VoObject>> returnObject = grouponServer.findShopGroupon(state, spuId, id, page, pageSize, beginTime, endTime);
        logger.debug("findgrouponActivity = " + returnObject);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 管理员查询某SPU所有团购活动
     *
     * @param id
     * @param state
     * @param shopId
     * @return
     * @author zhai

    @ApiOperation(value = "管理员查询某SPU所有团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "Skuid", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    //@Audit //需要认证
    @GetMapping("/shops/{shopId}/spus/{id}/groupons")
    public Object queryGrouponofSPU(@PathVariable Long id, Integer state, @PathVariable Long shopId
    ) {

        ReturnObject<List> returnObject;
        returnObject = grouponServer.findSkuGroupon(id, state, shopId);
        logger.debug("findgrouponActivity = " + returnObject);
        return Common.decorateReturnObject(returnObject);
    }
     */


    /**
     * 管理员对SPU新增团购活动
     *
     * @param id
     * @param grouponInputVo
     * @param bindingResult
     * @param shopId
     * @return
     * @Author zhai
     */
    @ApiOperation(value = "管理员对SPU新增团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺 id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "SpuId", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GrouponInputVo", name = "grouponInputVo", value = "可修改的团购活动信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @PostMapping("/shops/{shopId}/spus/{id}/groupons")
    public Object createGrouponofSPU(@PathVariable Long id, @Validated @RequestBody GrouponInputVo grouponInputVo, BindingResult bindingResult, @PathVariable Long shopId) {
        if (logger.isDebugEnabled()) {
            logger.debug("createGrouponofSPU: SpuId = " + id);
        }
        // 校验前端数据
        Object returnObj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObj != null) {
            logger.info("incorrect data received while createGrouponofSPU: spuId = ", id);
            return returnObj;
        }
        ReturnObject returnObject =null;
        SpuToGrouponVo spuToGrouponVo = goodservice.grouponFindSpu(id);
        if(spuToGrouponVo==null) {
            //Object returnObject = goodservice.echo2("123");
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("spuID不存在"));
        }else {
            ShopToAllVo shopToAllVo = inShopService.presaleFindShop(shopId);
            if (shopToAllVo == null) {
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("shopID不存在"));
            } else {
                GrouponActivityPo groupon = grouponServer.addGroupon(id, grouponInputVo, shopId);
                if(groupon==null){
                    return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW);
                }
                GrouponActivity grouponActivity=new GrouponActivity(groupon);
                returnObject = new ReturnObject<>(grouponActivity);
                GrouponOutputVo grouponOutputVo=new GrouponOutputVo(groupon);
                grouponOutputVo.setShopToAllVo(shopToAllVo);
                grouponOutputVo.setGoodsSpu(spuToGrouponVo);

                returnObject = new ReturnObject<>(grouponOutputVo);
            }
        }
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 管理员修改SPU团购活动
     *
     * @param id:            团购活动 id
     * @param grouponInputVo 可修改的团队活动信息
     * @return Object
     * @author zhai
     */
    @ApiOperation(value = "管理员修改SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺 id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "团购活动id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GrouponInputVo", name = "grouponInputVo", value = "可修改的团购活动信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @PutMapping("/shops/{shopId}/groupons/{id}")
    public Object changeGrouponofSPU(@PathVariable Long id, @Validated @RequestBody GrouponInputVo grouponInputVo, @PathVariable Long shopId) {
        System.out.println(grouponInputVo.getBeginTime());
        if (logger.isDebugEnabled()) {
            logger.debug("changeGroupon: grouponId = " + id);
        }

        ReturnObject returnObj = grouponServer.changeGroupon(id, grouponInputVo, shopId);
        return Common.decorateReturnObject(returnObj);


    }

    /**
     * 管理员下架SKU团购活动
     *
     * @param id
     * @param shopId
     * @return
     * @Author zhai
     */

    @ApiOperation(value = "管理员下架SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "团购活动Id")
    })
    @ApiResponses({
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @DeleteMapping("/shops/{shopId}/groupons/{id}")
    public Object cancelGrouponofSPU(@PathVariable Long id, @PathVariable Long shopId) {
        System.out.println("hello");
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGroupon: GrouponId = " + id);
        }
        ReturnObject returnObj = grouponServer.deleteGrouponState(shopId, id);
        return Common.decorateReturnObject(returnObj);

    }

    /**
     * 管理员下线SPU团购活动
     * @param id
     * @param shopId
     * @return
     * @author zhai
     */
    @ApiOperation(value = "管理员下线SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "团购活动Id")
    })
    @ApiResponses({
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @PutMapping("/shops/{shopId}/groupons/{id}/offshelves")
    public Object offGrouponofSPU(@PathVariable Long id, @PathVariable Long shopId) {
        System.out.println("hello");
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGroupon: GrouponId = " + id);
        }
        ReturnObject returnObj = grouponServer.offGrouponState(shopId, id);
        return Common.decorateReturnObject(returnObj);

    }

    /**
     * 管理员上线SPU团购活动
     * @param id
     * @param shopId
     * @return
     * @author  zhai
     */
    @ApiOperation(value = "管理员上线SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "团购活动Id")
    })
    @ApiResponses({
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    //@Audit // 需要认证
    @PutMapping("/shops/{shopId}/groupons/{id}/onshelves")
    public Object onGrouponofSPU(@PathVariable Long id, @PathVariable Long shopId) {
        System.out.println("hello");
        if (logger.isDebugEnabled()) {
            logger.debug("deleteGroupon: GrouponId = " + id);
        }
        ReturnObject returnObj = grouponServer.onGrouponState(shopId, id);
        return Common.decorateReturnObject(returnObj);

    }
}
