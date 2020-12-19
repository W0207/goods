package cn.edu.xmu.comment;

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
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
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

    private WebTestClient mallClient;



    public CommentControllerTest() {
        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8089")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

    }

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }

    /**
     * 伪造token
     */
    @Test
    public void create(){
        logger.debug("************************"+creatTestToken(1L,0L,1000*60*60*24)+"*****************");
    }
    /**
     * 获得评论所有状态
     */
    @Test
    public void getCommentState() throws Exception {
        String responseString = this.mvc.perform(get("/comment/comments/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未审核\", \"code\": 0 }, { \"name\": \"评论成功\", \"code\": 1 }, { \"name\": \"未通过\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getAllStates() throws Exception {
        byte[] ret = mallClient.get()
                .uri("/comments/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"未审核\",\"code\":0},{\"name\":\"评论成功\",\"code\":1},{\"name\":\"未通过\",\"code\":2}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 查看sku的评价列表（已通过审核）
     */

    @Test
    public void show() throws Exception {

        String responseString = this.mvc.perform(get("/comment/skus/273/comments?pageSize=10"))
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
                "  \"state\":\"true\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/comment/shops/0/confirm/2/confirm")
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

    /**
     * 买家查看自己的评价记录
     */

    @Test
    public void showComment() throws Exception {

        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/comment/comments").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void showComment1() throws Exception {

        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/comments/comments?page=1&pageSize=2").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员查看审核/未审核列表
     */

    @Test
    public void showUnAuditCommentsByCommentid() throws Exception {

        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/comment/shops/0/comments/all").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
