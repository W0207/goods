package cn.edu.xmu.presale.controller;

import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.presale.model.bo.PresaleActivity;
import cn.edu.xmu.presale.model.vo.PresaleActivityStateVo;
import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import cn.edu.xmu.presale.service.PresaleService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 **/
@Api(value = "预售服务", tags = "presale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/presale", produces = "application/json;charset=UTF-8")
public class PresaleController {

    private static final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @Autowired
    private PresaleService presaleService;

    @ApiOperation(value = "获得商品spu的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/presales/states")
    public Object getPresaleState(){
        logger.debug("getGoodsSpuState");
        PresaleActivity.State[] states = PresaleActivity.State.class.getEnumConstants();
        List<PresaleActivityStateVo> presaleActivityStateVos = new ArrayList<>();
        for (PresaleActivity.State state : states) {
            presaleActivityStateVos.add(new PresaleActivityStateVo(state));
        }
        return ResponseUtil.ok(new ReturnObject<List>(presaleActivityStateVos).getData());
    }


    /**
     * 管理员新增SPU预售活动
     * @param shopId
     * @param id
     * @param presaleActivityVo
     * @return
     */
    @ApiOperation("管理员新增SPU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "spuId"),
    })
    @PostMapping("/shops/{shopId}/spus/{id}/presales")
    public Object AddPresaleActivity(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityVo presaleActivityVo)
    {
        PresaleActivity presaleActivity = new PresaleActivity(presaleActivityVo);
        presaleActivity.setCreate(LocalDateTime.now());
        return Common.decorateReturnObject(presaleService.AddPresaleActivity(shopId,id,presaleActivity));
    }



}
