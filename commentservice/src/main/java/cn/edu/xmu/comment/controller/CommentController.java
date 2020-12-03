package cn.edu.xmu.comment.controller;

import cn.edu.xmu.comment.service.CommentService;
import cn.edu.xmu.comment.model.bo.Comment;
import cn.edu.xmu.comment.model.vo.CommentStateVo;
import cn.edu.xmu.comment.service.CommentService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 **/
@Api(value = "评论服务", tags = "comments")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/comments", produces = "application/json;charset=UTF-8")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

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
        pageSize = (pageSize == null) ? 60 : pageSize;
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
    @GetMapping("/states")
    public Object getCommentState() {
        logger.debug("getCommentState");
        Comment.State[] states = Comment.State.class.getEnumConstants();
        List<CommentStateVo> commentStateVos = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            commentStateVos.add(new CommentStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(commentStateVos).getData());
    }
}
