package cn.edu.xmu.groupon.controller;

import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.po.*;
import cn.edu.xmu.groupon.model.vo.*;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.edu.xmu.groupon.service.GrouponServer;
import cn.edu.xmu.ooad.annotation.Audit;
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
import cn.edu.xmu.ininterface.service.model.vo.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cn.edu.xmu.ininterface.service.*;

/**
 * 权限控制器
 *
 * @author zhai
 */
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

    private int getStatue(ReturnObject returnObject) {
        if (returnObject.getCode() == ResponseCode.RESOURCE_ID_OUTSCOPE) {
            return HttpStatus.FORBIDDEN.value();
        }
        if (returnObject.getCode() == ResponseCode.FIELD_NOTVALID || returnObject.getCode() == ResponseCode.Log_Bigger || returnObject.getCode() == ResponseCode.Log_BEGIN_NULL || returnObject.getCode() == ResponseCode.Log_END_NULL) {
            return HttpStatus.BAD_REQUEST.value();
        }
        if (returnObject.getCode() == ResponseCode.ACTIVITYALTER_INVALID) {
            return HttpStatus.BAD_REQUEST.value();
        }
        if (returnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
            return HttpStatus.NOT_FOUND.value();
        }
        return HttpStatus.OK.value();
    }

    private int createStatue(ReturnObject returnObject) {
        if (returnObject.getCode() == ResponseCode.RESOURCE_ID_OUTSCOPE) {
            return HttpStatus.FORBIDDEN.value();
        }
        if (returnObject.getCode() == ResponseCode.FIELD_NOTVALID || returnObject.getCode() == ResponseCode.Log_Bigger || returnObject.getCode() == ResponseCode.Log_BEGIN_NULL || returnObject.getCode() == ResponseCode.Log_END_NULL) {
            return HttpStatus.BAD_REQUEST.value();
        }
        if (returnObject.getCode() == ResponseCode.ACTIVITYALTER_INVALID) {
            return HttpStatus.BAD_REQUEST.value();
        }
        if (returnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
            return HttpStatus.NOT_FOUND.value();
        }
        return HttpStatus.CREATED.value();
    }

    /**
     * 获得团购活动的所有状态
     *
     * @return Object
     * @author zhai
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
     * @author zhai
     */
    @ApiOperation(value = "查询所有团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spuId", value = "spuId", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "shopId", value = "商店id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/groupons")
    public Object queryGroupons(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Integer timeline,
            @RequestParam(required = false) Long spuId,
            HttpServletResponse response
    ) {

        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page < 0 || pageSize < 0) {
            ReturnObject returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
            response.setStatus(getStatue(returnObject));
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID, String.format("店铺不存在")));

        }
        ReturnObject<PageInfo<VoObject>> returnObject = grouponServer.findgrouponActivity(timeline, spuId, shopId, page, pageSize);
        logger.debug("findGrouponActivity = " + returnObject);
        response.setStatus(getStatue(returnObject));
        return Common.getPageRetObject(returnObject);

    }


    /**
     * 管理员查询所有团购（包括下线,删除的）
     *
     * @return Object
     * @author zhai
     **/
    @ApiOperation(value = "管理员查询所有团购（包括下线,删除的）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商店id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "spuId", value = "spuId", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/shops/{id}/groupons")
    public Object queryGroupon(@PathVariable Long id,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer pageSize,
                               @RequestParam(required = false) String beginTime,
                               @RequestParam(required = false) String endTime,
                               @RequestParam(required = false) Integer state,
                               @RequestParam(required = false) Long spuId,
                               HttpServletResponse response
    ) {

        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page < 0 || pageSize < 0) {
            ReturnObject returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
            response.setStatus(getStatue(returnObject));
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID, String.format("店铺不存在")));

        }
        boolean bool = inShopService.shopExitOrNot(id);
        if (!bool) {
            logger.info("该店铺不存在");
            ReturnObject returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            response.setStatus(getStatue(returnObject));
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在")));
        }
        ReturnObject<PageInfo<VoObject>> returnObject = grouponServer.findShopGroupon1(state, spuId, id, page, pageSize, beginTime, endTime);
        logger.debug("findGrouponActivity = " + returnObject);
        response.setStatus(getStatue(returnObject));
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 管理员对SPU新增团购活动
     *
     * @param id
     * @param grouponInputVo
     * @param bindingResult
     * @param shopId
     * @return
     * @author zhai
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
    // @Audit
    @PostMapping("/shops/{shopId}/spus/{id}/groupons")
    public Object createGrouponofSPU(@PathVariable @NotNull Long id, @NotNull @Validated @RequestBody GrouponInputVo grouponInputVo, BindingResult bindingResult, @PathVariable Long shopId, HttpServletResponse response) {

//         //校验前端数据
//        Object returnObj = Common.processFieldErrors(bindingResult, httpServletResponse);
//        if (returnObj != null) {
//            logger.info("incorrect data received while createGrouponofSPU: spuId = ", id);
//            return returnObj;
//        }

        ReturnObject returnObject = grouponServer.addGroupon(id, grouponInputVo, shopId);
        response.setStatus(createStatue(returnObject));
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
    @Audit
    @PutMapping("/shops/{shopId}/groupons/{id}")
    public Object changeGrouponofSPU(@PathVariable Long id, @Validated @RequestBody GrouponInputVo grouponInputVo, @PathVariable Long shopId, HttpServletResponse response) {
        ReturnObject returnObject = grouponServer.changeGroupon(id, grouponInputVo, shopId);
        response.setStatus(getStatue(returnObject));
        return Common.decorateReturnObject(returnObject);


    }

    /**
     * 管理员下架SKU团购活动
     *
     * @param id
     * @param shopId
     * @return
     * @author zhai
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
    @Audit
    @DeleteMapping("/shops/{shopId}/groupons/{id}")
    public Object cancelGrouponofSPU(@PathVariable Long id, @PathVariable Long shopId, HttpServletResponse response) {
        ReturnObject returnObj = grouponServer.deleteGrouponState(shopId, id);
        response.setStatus(getStatue(returnObj));
        return Common.decorateReturnObject(returnObj);

    }

    /**
     * 管理员下线SPU团购活动
     *
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
    @Audit
    @PutMapping("/shops/{shopId}/groupons/{id}/offshelves")
    public Object offGrouponofSPU(@PathVariable Long id, @PathVariable Long shopId, HttpServletResponse response) {
        ReturnObject returnObj = grouponServer.offGrouponState(shopId, id);
        response.setStatus(getStatue(returnObj));
        return Common.decorateReturnObject(returnObj);

    }

    /**
     * 管理员上线SPU团购活动
     *
     * @param id
     * @param shopId
     * @return
     * @author zhai
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
    @Audit
    @PutMapping("/shops/{shopId}/groupons/{id}/onshelves")
    public Object onGrouponofSPU(@PathVariable Long id, @PathVariable Long shopId, HttpServletResponse response) {
        ReturnObject returnObj = grouponServer.onGrouponState(shopId, id);
        response.setStatus(getStatue(returnObj));
        return Common.decorateReturnObject(returnObj);

    }
}
