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
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /**
     * 查看sku的评价列表（已通过审核） 未集成可通过
     */

    @Test
    public void show1() throws Exception {

        String responseString = this.mvc.perform(get("/comment/skus/273/comments?pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\n" +
                "\"errno\": 0,\n" +
                "\"data\":{\n" +
                "\"total\": 2,\n" +
                "\"pages\": 2,\n" +
                "\"pageSize\": 1,\n" +
                "\"page\": 1,\n" +
                "\"list\":[\n" +
                "{\n" +
                "\"id\": 2,\n" +
                "\"customerId\": 1,\n" +
                "\"orderitemId\": 2,\n" +
                "\"goodsSkuId\": 273,\n" +
                "\"type\": 2,\n" +
                "\"content\": \"挺好的\",\n" +
                "\"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "\"gmtModified\": \"2020-12-10T22:36:01\",\n" +
                "\"customer\": null\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "\"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    @Test
    public void show2() throws Exception {

        String responseString = this.mvc.perform(get("/comment/skus/274/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\n" +
                "\"errno\": 0,\n" +
                "\"data\":{\n" +
                "\"total\": 0,\n" +
                "\"pages\": 0,\n" +
                "\"pageSize\": 10,\n" +
                "\"page\": 1,\n" +
                "\"list\":[]\n" +
                "},\n" +
                "\"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    @Test
    public void show3() throws Exception {

        String responseString = this.mvc.perform(get("/comment/skus/185/comments?pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\n" +
                "\"errno\": 0,\n" +
                "\"data\":{\n" +
                "\"total\": 5,\n" +
                "\"pages\": 3,\n" +
                "\"pageSize\": 2,\n" +
                "\"page\": 1,\n" +
                "\"list\":[\n" +
                "{\n" +
                "\"id\": 6,\n" +
                "\"customerId\": 1,\n" +
                "\"orderitemId\": 6,\n" +
                "\"goodsSkuId\": 185,\n" +
                "\"type\": 0,\n" +
                "\"content\": \"真不错\",\n" +
                "\"gmtCreate\": \"2020-12-13T13:48:44\",\n" +
                "\"gmtModified\": \"2020-12-13T13:48:44\",\n" +
                "\"customer\": null\n" +
                "},\n" +
                "{\n" +
                "\"id\": 13,\n" +
                "\"customerId\": 1,\n" +
                "\"orderitemId\": 15,\n" +
                "\"goodsSkuId\": 185,\n" +
                "\"type\": 0,\n" +
                "\"content\": \"还行\",\n" +
                "\"gmtCreate\": \"2020-12-13T13:48:44\",\n" +
                "\"gmtModified\": \"2020-12-13T13:48:44\",\n" +
                "\"customer\": null\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "\"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    /**
     * 管理员审核评论
     */
    @Test
    public void auditComment1() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"true\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/comment/shops/0/comments/1/confirm")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void auditComment2() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"false\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/comment/shops/0/comments/1/confirm")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":7,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":2,\"customerId\":1,\"orderitemId\":2,\"goodsSkuId\":273,\"type\":2,\"content\":\"挺好的\",\"gmtCreate\":\"2020-12-10T22:36:01\",\"gmtModified\":\"2020-12-10T22:36:01\",\"customer\":null},{\"id\":3,\"customerId\":1,\"orderitemId\":3,\"goodsSkuId\":273,\"type\":1,\"content\":\"哇偶\",\"gmtCreate\":\"2020-12-10T22:36:01\",\"gmtModified\":\"2020-12-10T22:36:01\",\"customer\":null},{\"id\":6,\"customerId\":1,\"orderitemId\":6,\"goodsSkuId\":185,\"type\":0,\"content\":\"真不错\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null},{\"id\":13,\"customerId\":1,\"orderitemId\":15,\"goodsSkuId\":185,\"type\":0,\"content\":\"还行\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null},{\"id\":14,\"customerId\":1,\"orderitemId\":16,\"goodsSkuId\":185,\"type\":0,\"content\":\"还行\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null},{\"id\":15,\"customerId\":1,\"orderitemId\":17,\"goodsSkuId\":185,\"type\":0,\"content\":\"还行\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null},{\"id\":16,\"customerId\":1,\"orderitemId\":18,\"goodsSkuId\":185,\"type\":0,\"content\":\"还行\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    @Test
    public void showComment1() throws Exception {

        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/comment/comments?page=1&pageSize=2").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":7,\"pages\":4,\"pageSize\":2,\"page\":1,\"list\":[{\"id\":2,\"customerId\":1,\"orderitemId\":2,\"goodsSkuId\":273,\"type\":2,\"content\":\"挺好的\",\"gmtCreate\":\"2020-12-10T22:36:01\",\"gmtModified\":\"2020-12-10T22:36:01\",\"customer\":null},{\"id\":3,\"customerId\":1,\"orderitemId\":3,\"goodsSkuId\":273,\"type\":1,\"content\":\"哇偶\",\"gmtCreate\":\"2020-12-10T22:36:01\",\"gmtModified\":\"2020-12-10T22:36:01\",\"customer\":null}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员查看审核/未审核列表
     */

    @Test
    public void showUnAuditCommentsByCommentid() throws Exception {

        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/comment/shops/0/comments/all?page=2&pageSize=2").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":5,\"pages\":3,\"pageSize\":2,\"page\":2,\"list\":[{\"id\":8,\"customerId\":1,\"orderitemId\":8,\"goodsSkuId\":185,\"type\":0,\"content\":\"不好吃\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null},{\"id\":9,\"customerId\":1,\"orderitemId\":9,\"goodsSkuId\":185,\"type\":0,\"content\":\"不好吃\",\"gmtCreate\":\"2020-12-13T13:48:44\",\"gmtModified\":\"2020-12-13T13:48:44\",\"customer\":null}]},\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }



}
