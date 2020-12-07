package cn.edu.xmu.comment;

import cn.edu.xmu.comment.CommentServiceApplication;
import cn.edu.xmu.comment.controller.CommentController;
import cn.edu.xmu.comment.mapper.CommentPoMapper;
import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CommentServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    CommentPoMapper commentPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }

    /**
     * 获得评论所有状态
     */
    @Test
    public void getCommentState() throws Exception {
        String responseString = this.mvc.perform(get("/comments/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未审核\", \"code\": 0 }, { \"name\": \"评论成功\", \"code\": 1 }, { \"name\": \"未通过\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 查看sku的评价列表（已通过审核）
     */

    @Test
    public void show() throws Exception {

        String responseString = this.mvc.perform(get("/comments/skus/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员审核评论
     */
    @Test
    public void auditComment() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"0\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/comments/shops/0/confirm/1/confirm")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        CommentPo commentPo = commentPoMapper.selectByPrimaryKey(1L);
        System.out.println(commentPo.getState());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
