package cn.edu.xmu.groupon.controller;

import cn.edu.xmu.groupon.GrouponServiceApplication;
import cn.edu.xmu.groupon.controller.GrouponController;
import cn.edu.xmu.groupon.mapper.GrouponActivityPoMapper;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Order;
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

@SpringBootTest(classes = GrouponServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class GrouponControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    GrouponActivityPoMapper grouponActivityPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(GrouponController.class);


    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }
//    private WebTestClient mallClient;
//
//
//    public GrouponControllerTest() {
//        this.mallClient = WebTestClient.bindToServer()
//                .baseUrl("http://localhost:8084")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
//                .build();
//    }




    @Order(1)
    @Test
    public void getgrouponState() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"已下线\", \"code\": 0 }, { \"name\": \"已上线\", \"code\": 1 }, { \"name\": \"已删除\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(2)
    @Test
    public void queryGroupons1() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = " {\"errno\":0,\"data\":{\"total\":5,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1,\"name\":\"双十一\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":2,\"name\":\"双十二\",\"beginTime\":\"2020-12-29T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":3,\"name\":\"黑色星期五\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":4,\"name\":\"儿童节\",\"beginTime\":\"2020-06-01T11:57:39\",\"endTime\":\"2020-06-02T11:57:39\"},{\"id\":5,\"name\":\"劳动节\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"}]},\"errmsg\":\"成功\"}\n";
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(3)
    @Test
    public void queryGroupons2() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons?timeline=0")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\n" +
                "\"errno\": 0,\n" +
                "\"data\":{\n" +
                "\"total\": 1,\n" +
                "\"pages\": 1,\n" +
                "\"pageSize\": 10,\n" +
                "\"page\": 1,\n" +
                "\"list\":[\n" +
                "{\n" +
                "\"id\": 2,\n" +
                "\"name\": \"双十二\",\n" +
                "\"beginTime\": \"2020-12-29T11:57:39\",\n" +
                "\"endTime\": \"2020-12-30T11:57:39\"\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "\"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(4)
    @Test
    public void queryGroupons3() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons?timeline=1")
                .header("authorization", token))
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
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(5)
    @Test
    public void queryGroupons4() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons?timeline=2")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"data\":{\"total\":3,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1,\"name\":\"双十一\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":3,\"name\":\"黑色星期五\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":5,\"name\":\"劳动节\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"}]},\"errmsg\":\"成功\"}";
       // JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(6)
    @Test
    public void queryGroupons5() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons?timeline=3")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        //String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":4,\"name\":\"儿童节\",\"beginTime\":\"2020-06-01T11:57:39\",\"endTime\":\"2020-06-02T11:57:39\"}]},\"errmsg\":\"成功\"}\n";
       // JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(7)
    @Test
    public void queryGroupons6() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons?timeline=2&spuId=273&shopId=1")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //System.out.println(responseString);
        //String expectedResponse = "{\"errno\":0,\"data\":{\"total\":3,\"pages\":1,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1,\"name\":\"双十一\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":3,\"name\":\"黑色星期五\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"},{\"id\":5,\"name\":\"劳动节\",\"beginTime\":\"2020-12-05T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"}]},\"errmsg\":\"成功\"}\n";
       // JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Order(8)
    @Test
    public void queryGroupons7() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons?page=2&pageSize=1")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
//        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":5,\"pages\":5,\"pageSize\":1,\"page\":2,\"list\":[{\"id\":2,\"name\":\"双十二\",\"beginTime\":\"2020-12-29T11:57:39\",\"endTime\":\"2020-12-30T11:57:39\"}]},\"errmsg\":\"成功\"}";
//        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Order(9)
    @Test
    public void queryGroupon() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/shops/1/groupons")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }



    @Test
    public void createGrouponofSPU() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"begintime\":\"2021-11-28T17:42:20\",\n" +
                "  \"endtime\": \"2021-12-28T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/groupon/shops/1/spus/1/groupons")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
//        System.out.println(responseString);
        // JSONAssert.assertEquals(expectedResponse, responseString, true);
    }



    @Test
    public void test() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-28T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //开始时间大于结束时间
    }
    @Test
    public void test1() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-20T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //正常
    }

    @Test
    public void test2() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2020-11-28T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //开始时间小于当前时间
    }

    @Test
    public void test3() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-20T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/2/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //活动不属于该店铺
    }

    @Test
    public void test4() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-20T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/10/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //找不到店铺
    }

    @Test
    public void test5() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //开始时间为空
    }

    @Test
    public void test6() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-20T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/10")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //找不到团购活动
    }

    @Test
    public void test7() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-20T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/4")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //团购活动已上线
    }

    @Test
    public void test8() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-20T17:42:20\",\n" +
                "  \"endTime\": \"2021-11-21T17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/2")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //团购活动已删除
    }

    @Test
    public void test9() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"beginTime\":\"2021-11-21T17:42:20\",\n" +
                "  \"endTime\": \"\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //结束时间为空
    }


    @Test
    public void cancelGrouponofSPU() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(delete("/groupon/shops/1/groupons/1")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
//        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }//正常

    @Test
    public void cancelGrouponofSPU1() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(delete("/groupon/shops/10/groupons/1")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
//        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }//找不到该店铺

    @Test
    public void cancelGrouponofSPU2() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(delete("/groupon/shops/1/groupons/10")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
//        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }//找不到该活动

    @Test
    public void cancelGrouponofSPU3() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(delete("/groupon/shops/1/groupons/10")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
//        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }//找不到该活动

    @Test
    public void onGrouponofSPU() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/groupon/shops/1/groupons/1/onshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Test
    public void offGrouponofSPU() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/groupon/shops/0/groupons/72/offshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }



}
