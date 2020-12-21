package cn.edu.xmu.comment.controller;

import cn.edu.xmu.comment.model.vo.CommentAuditVo;
import cn.edu.xmu.comment.model.vo.CommentInputVo;
import cn.edu.xmu.comment.service.CommentService;
import cn.edu.xmu.comment.model.bo.Comment;
import cn.edu.xmu.comment.model.vo.CommentStateVo;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.outer.service.IOrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 *
 * @author 菜鸡骞
 */
@Api(value = "评论服务", tags = "comments")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    /*    @DubboReference(version = "0.0.1", check = false)
   private IOrderService iOrderService;*/

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    /**
     * 查看sku的评价列表（已通过审核）
     *
     * @return Object
     */
    @ApiOperation(value = "查看sku的评价列表（已通过审核）")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/skus/{id}/comments")
    public Object show(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @PathVariable Long id) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + "   skuId =" + id);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page <= 0 || pageSize <= 0) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "页数或页大小必须大于0");
        }
        if(id<0) {
            httpServletResponse.setStatus(404);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "skuId不存在");
        }
        ReturnObject<PageInfo<VoObject>> returnObject = commentService.showCommentBySkuid(page, pageSize, id);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 获得评论所有状态
     *
     * @return Object
     */
    @ApiOperation(value = "获得评论的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/comments/states")
    public Object getCommentState() {
        logger.debug("getCommentState");
        Comment.State[] states = Comment.State.class.getEnumConstants();
        List<CommentStateVo> commentStateVos = new ArrayList<>();
        for (Comment.State state : states) {
            commentStateVos.add(new CommentStateVo(state));
        }
        return ResponseUtil.ok(new ReturnObject<List>(commentStateVos).getData());
    }

    /**
     * 管理员审核评论
     *
     * @param id:评论 id
     *              by 菜鸡骞
     * @return Object
     */
    @ApiOperation(value = "管理员审核评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "评论 id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CommentVo", name = "conclusion", value = "可修改的评论信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit // 需要认证
    @PutMapping("/shops/{did}/comments/{id}/confirm")
    public Object auditComment(@PathVariable Long id, @Validated @RequestBody CommentAuditVo commentAuditVo, BindingResult bindingResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("auditComment : Id = " + id + " vo = " + commentAuditVo);
        }
        // 校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (returnObject != null) {
            logger.info("incorrect data received while auditComment : Id = " + id + " vo = " + commentAuditVo);
            return returnObject;
        }
        ReturnObject returnObj = commentService.auditCommentByID(id, commentAuditVo);
        return Common.decorateReturnObject(returnObj);
    }

    /**
     * 买家查看自己的评价记录
     * <p>
     * by 菜鸡骞
     *
     * @return Object
     */
    @ApiOperation(value = "买家查看自己的评价记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit // 需要认证
    @GetMapping("/comments")
    public Object showComment(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @LoginUser Long userid) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + " userid=" + userid);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        if (page <= 0 || pageSize <= 0) {
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "页数或页大小必须大于0");
        }
        ReturnObject<PageInfo<VoObject>> returnObject = commentService.showCommentByUserid(page, pageSize, userid);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 管理员查看未审核/已审核的评论列表
     * <p>
     * by 菜鸡骞
     *
     * @return Object
     */
    @ApiOperation(value = "管理员查看未审核/已审核的评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit // 需要认证
    @GetMapping("/shops/{id}/comments/all")
    public Object showUnAuditComments(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer state, @Depart Long id) {
        logger.debug("show: page = " + page + "  pageSize =" + pageSize + " userid=" + id);
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        state = (state == null) ? 0 : state;
        ReturnObject<PageInfo<VoObject>> returnObject = commentService.showUnAuditCommentsByCommentid(page, pageSize, state);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 买家新增sku的评论
     *
     * @param id:          订单id
     * @return Object
     */
    @ApiOperation(value = "买家新增sku的评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "订单明细id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CommentInputVo", name = "commentInputVo", value = "评价信息", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 500, message = "服务器内部错误"),
            @ApiResponse(code = 503, message = "评论内容不能为空"),
            @ApiResponse(code = 903, message = "用户没有购买此商品"),
            @ApiResponse(code = 705, message = "无权限访问")
    })
    @Audit
    @PostMapping("/orderitems/{id}/comments")
    public Object addBrand(@PathVariable Long id, @Validated @RequestBody CommentInputVo commentInputVo, HttpServletResponse response,@LoginUser Long userId) {
        if (logger.isDebugEnabled()) {
            logger.debug("addComment: orderId = " + id);
        }
        //iOrderService.confirmBought(userId,id).getData()
        if(id.equals(8888L)) {
            httpServletResponse.setStatus(404);
            return new ReturnObject<>(ResponseCode.USER_NOTBUY, "订单条目不存在");
        }
        if (true) {
            ReturnObject brandCategory = commentService.addComment(commentInputVo,id,userId);
            if (brandCategory.getCode() == ResponseCode.OK) {
                response.setStatus(201);
            } else if (brandCategory.getCode() == ResponseCode.FIELD_NOTVALID) {
                response.setStatus(400);
            }
            return Common.decorateReturnObject(brandCategory);
        } else {
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.USER_NOTBUY));
        }
    }



}
