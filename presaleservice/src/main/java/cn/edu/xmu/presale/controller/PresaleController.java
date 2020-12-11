package cn.edu.xmu.presale.controller;

import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.presale.model.bo.PresaleActivity;
import cn.edu.xmu.presale.model.vo.PresaleActivityRetVo;
import cn.edu.xmu.presale.model.vo.PresaleActivityStateVo;
import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import cn.edu.xmu.presale.service.PresaleService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 **/
@Api(value = "预售服务", tags = "presale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/presale", produces = "application/json;charset=UTF-8")
public class PresaleController {

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private InShopService inShopService;


    private static final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @Autowired
    private PresaleService presaleService;


    @ApiOperation(value = "获得商品spu的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/presales/states")
    public Object getPresaleState() {
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
    /**
     * @ApiOperation("管理员新增SPU预售活动")
     * @ApiImplicitParams({
     * @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
     * @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "spuId"),
     * })
     * @PostMapping("/shops/{shopId}/spus/{id}/presales") public Object AddPresaleActivity(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityVo presaleActivityVo)
     * {
     * PresaleActivity presaleActivity = new PresaleActivity(presaleActivityVo);
     * presaleActivity.setCreate(LocalDateTime.now());
     * return Common.decorateReturnObject(presaleService.AddPresaleActivity(shopId,id,presaleActivity));
     * }
     */

    @ApiOperation("管理员新增SPU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "spuId"),
    })
    @PostMapping("/shops/{shopId}/skus/{id}/presales")
    public Object AddPresaleActivity(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityVo presaleActivityVo) {
        System.out.println("aaaaa");
        ReturnObject returnObject = null;
        returnObject = presaleService.AddPresaleActivity(shopId, id, presaleActivityVo);
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * 查询所有有效的活动
     *
     * @param shopId
     * @param timeLine
     * @param skuId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询所有有效的活动")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("presales")
    public Object selectAllPresale(
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) Integer timeLine,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize

    ) {
        logger.debug("selectAllRoles: shopId = " + shopId + "  timeLine =" + timeLine + "  spuId =" + skuId + "  page = " + page + "  pageSize" + pageSize);
        ReturnObject returnObject = presaleService.selectAllPresale(shopId, timeLine, skuId, page, pageSize);
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * @param shopId
     * @param id
     * @param vo
     * @return
     */
    @ApiOperation(value = "管理员修改sku预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "spuId"),
    })
    //@Audit
    @PutMapping("/shops/{shopId}/presales/{id}")
    public Object modifyPresale(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityVo vo) {
        return Common.decorateReturnObject(presaleService.modifyPresale(shopId, id, vo));
    }


    /**
     * @param shopId
     * @param id
     * @return
     */
    @ApiOperation(value = "管理员删除sku预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "spuId"),
    })
    //@Audit
    @DeleteMapping("/shops/{shopId}/presales/{id}")
    public Object deletePresale(@PathVariable Long shopId, @PathVariable Long id) {
        return Common.decorateReturnObject(presaleService.deletePresale(shopId, id));
    }


    /**
     * @param shopId
     * @param id
     * @param state
     * @return
     */
    @ApiOperation(value = "管理员查询SPU所有预售活动(包括下线的)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "skuId"),
            @ApiImplicitParam(name = "state", required = false, dataType = "Integer", paramType = "path", value = "state")
    })
    @GetMapping("/shops/{shopId}/skus/{id}/presales")
    public Object queryPresaleofSPU(@PathVariable Long shopId, @PathVariable Long id, @RequestParam Integer state) {
        ReturnObject returnObject = presaleService.queryPresaleofSKU(shopId, id, state);
        return Common.decorateReturnObject(returnObject);
    }

    @ApiOperation(value = "管理员上线预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "skuId")
    })
    @PutMapping("/shops/{shopId}/skus/{id}/presales")
    public Object presaleOnShelves(@PathVariable Long shopId, @PathVariable Long id) {
        return Common.decorateReturnObject(presaleService.presaleOnShelves(shopId, id));
    }

    @ApiOperation(value = "管理员下线预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path", value = "shopId"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path", value = "skuId")
    })
    @PutMapping("/shops/{shopId}/skus/{id}/presales")
    public Object presaleOffShelves(@PathVariable Long shopId, @PathVariable Long id) {
        return Common.decorateReturnObject(presaleService.presaleOffShelves(shopId, id));
    }

}
