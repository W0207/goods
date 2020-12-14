package cn.edu.xmu.groupon.controller;

import cn.edu.xmu.groupon.GrouponServiceApplication;
import cn.edu.xmu.groupon.controller.GrouponController;
import cn.edu.xmu.groupon.mapper.GrouponActivityPoMapper;
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
    private WebTestClient mallClient;


    public GrouponControllerTest() {
        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8084")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    /**
     * 获得团购活动的所有状态
     */
    @Test
    public void getgrouponState() throws Exception {
        String responseString = this.mvc.perform(get("/groupon/groupons/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"已下线\", \"code\": 0 }, { \"name\": \"已上线\", \"code\": 1 }, { \"name\": \"已删除\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 查看所有的团购活动(上线的)
     */
    @Test
    public void queryGroupons() throws Exception {
        String token1 = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/groupon/groupons")
                .header("authorization", token1))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 查看所有团购活动包括下线，删除的
     */
    @Test
    public void queryGroupon() throws Exception {
        String responseString = this.mvc.perform(get("/groupon/shops/1/groupons"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }


    /**
     * 管理员新增团购活动
     *
     * @throws Exception
     */
    @Test
    public void createGrouponofSPU() throws Exception {
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"begintime\":\"2020-11-28 17:42:20\",\n" +
                "  \"endtime\": \"2020-11-28 17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/groupon/shops/1/spus/285/groupons")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        // JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /**
     * 修改SPU团购活动
     */
    @Test
    public void test() throws Exception {
        String requireJson = "{\n" +
                "  \"strategy\": 123,\n" +
                "  \"begintime\":\"2020-11-28 17:42:20\",\n" +
                "  \"endtime\": \"2020-11-28 17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/groupon/shops/0/groupons/71")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 下架团购活动
     */
    @Test
    public void cancelGrouponofSPU() throws Exception {
        String responseString = this.mvc.perform(delete("/groupon/shops/0/groupons/71")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 上线团购活动
     *
     * @throws Exception
     */
    @Test
    public void onGrouponofSPU() throws Exception {
        String responseString = this.mvc.perform(put("/groupon/shops/0/groupons/71/onshelves")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 下线团购活动
     *
     * @throws Exception
     */
    @Test
    public void offGrouponofSPU() throws Exception {
        String responseString = this.mvc.perform(put("/groupon/shops/0/groupons/72/offshelves")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }



    /* 公开测试             */
    @Test
    public void getGrouponAllStates() throws Exception {
        byte[] ret = mallClient.get()
                .uri("/groupon/groupons/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"已新建\",\"code\":0}," +
                "{\"name\":\"被取消\",\"code\":1}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.post()
                .uri("/groupon/shops/1/spus/273/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.println(responseString);
        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":5,\"name\":null," +
                "\"goodsSpu\":{\"id\":273,\"name\":\"金和汇景•戴荣华•古彩洛神赋瓷瓶\"," +
                "\"goodsSn\":\"drh-d0001\",\"imageUrl\":" +
                "\"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\"," +
                "\"state\":4,\"gmtCreate\":\"2020-12-03 21:04:55\"," +
                "\"gmtModified\":\"2020-12-03 21:04:55\",\"disable\":4}," +
                "\"shop\":{\"id\":1,\"name\":\"Nike\"},\"strategy\":\"无\",\"state\":0," +
                "\"beginTime\":\"2021-12-07 11:57:39\",\"endTime\":\"2021-12-09 11:57:39\"}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-12 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.post()
                .uri("/groupon/shops/1/spus/273/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("916")
                .jsonPath("$.errmsg").isEqualTo("结束时间在开始时间之前")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":916,\"errmsg\":\"结束时间在开始时间之前\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-03 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.post()
                .uri("/groupon/shops/1/spus/273/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("917")
                .jsonPath("$.errmsg").isEqualTo("开始时间已过")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":917,\"errmsg\":\"开始时间已过\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.post()
                .uri("/groupon/shops/1/spus/274/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("919")
                .jsonPath("$.errmsg").isEqualTo("该商品不在该店铺内")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":919,\"errmsg\":\"该商品不在该店铺内\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Test
    public void createGrouponAc6() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.post()
                .uri("/groupon/shops/10/spus/274/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc7() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.post()
                .uri("/groupon/shops/1/spus/1/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("945")
                .jsonPath("$.errmsg").isEqualTo("没有该商品")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":945,\"errmsg\":\"没有该商品\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.put()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-12 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.put()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("916")
                .jsonPath("$.errmsg").isEqualTo("结束时间在开始时间之前")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":916,\"errmsg\":\"结束时间在开始时间之前\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-03 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.put()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("917")
                .jsonPath("$.errmsg").isEqualTo("开始时间已过")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":917,\"errmsg\":\"开始时间已过\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.put()
                .uri("/groupon/shops/2/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("924")
                .jsonPath("$.errmsg").isEqualTo("活动不属于该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":924,\"errmsg\":\"活动不属于该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc5() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.put()
                .uri("/groupon/shops/10/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Test
    public void changeGrouponAc7() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = mallClient.put()
                .uri("/groupon/shops/1/groupons/10")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("946")
                .jsonPath("$.errmsg").isEqualTo("没有该活动")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":946,\"errmsg\":\"没有该活动\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/groupon/shops/2/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("924")
                .jsonPath("$.errmsg").isEqualTo("活动不属于该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":924,\"errmsg\":\"活动不属于该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/groupon/shops/10/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Test
    public void cancelGrouponAc5() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/groupon/shops/1/groupons/10")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("946")
                .jsonPath("$.errmsg").isEqualTo("没有该活动")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":946,\"errmsg\":\"没有该活动\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getGrouponActivity1() throws Exception {
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",1L)
                        .queryParam("timeline",2L)
                        .queryParam("spuId",1L)
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":" +
                "{\"total\":2,\"pages\":1,\"pageSize\":2,\"page\":1," +
                "\"list\":[{\"id\":1,\"name\":\"双十一\",\"beginTime\":\"2020-12-05 11:57:39\"," +
                "\"endTime\":\"2020-12-09 11:57:39\"},{\"id\":3,\"name\":\"黑色星期五\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getGrouponActivity2() throws Exception {
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",2L)
                        .queryParam("timeline",0L)
                        .queryParam("spuId",2L)
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":2,\"name\":\"双十二\"," +
                "\"beginTime\":\"2020-12-07 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getGrouponActivity3() throws Exception {
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",2L)
                        .queryParam("timeline",1L)
                        .queryParam("spuId",2L)
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":2,\"name\":\"双十二\"," +
                "\"beginTime\":\"2020-12-07 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getGrouponActivity4() throws Exception {
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",2L)
                        .queryParam("timeline",3L)
                        .queryParam("spuId",2L)
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedString = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":4,\"name\":\"儿童节\"," +
                "\"beginTime\":\"2020-06-01 11:57:39\",\"endTime\":\"2020-06-02 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getOneGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/spus/1/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("945")
                .jsonPath("$.errmsg").isEqualTo("没有该商品")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":945,\"errmsg\":\"没有该商品\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/10/spus/273/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/2/spus/273/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("919")
                .jsonPath("$.errmsg").isEqualTo("该商品不在该店铺内")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":919,\"errmsg\":\"该商品不在该店铺内\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/spus/273/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Test
    public void getShopGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/groupons")
                        .queryParam("state",(byte) 0)
                        .queryParam("spuId",273L)
                        .queryParam("startTime","2020-12-01 11:57:39")
                        .queryParam("endTime","2020-12-12 11:57:39")
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1," +
                "\"pageSize\":1,\"page\":1,\"list\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}" +
                ",\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getShopGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/groupons")
                        .queryParam("state",(byte) 0)
                        .queryParam("spuId",273L)
                        .queryParam("startTime","null")
                        .queryParam("endTime","2020-12-12 11:57:39")
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1," +
                "\"pageSize\":1,\"page\":1,\"list\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}" +
                ",\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getShopGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/groupons")
                        .queryParam("state",(byte) 0)
                        .queryParam("spuId",273L)
                        .queryParam("startTime","2020-12-01 11:57:39")
                        .queryParam("endTime","null")
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1," +
                "\"pageSize\":1,\"page\":1,\"list\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}" +
                ",\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
