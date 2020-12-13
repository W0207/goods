package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.controller.ShopController;
import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.json.JSONException;
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

@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc    //配置模拟的MVC，这样可以不启动服务器测试
@Transactional
public class ShopTest {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }

    private WebTestClient mallClient;


    public ShopTest() {
        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    @Autowired
    ShopPoMapper shopPoMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    public void insertShopTest() {
        ShopVo vo = new ShopVo();
        vo.setName("test");
        String shopJson = JacksonUtil.toJson(vo);
        String expectedResponse = "";
        String responseString = null;
        try {
            responseString = this.mvc.perform(post("/shop/shops").contentType("application/json;charset=UTF-8").content(shopJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //expectedResponse = "{\"errno\":0,\"data\":{\"id\":1,\"name\":\"test\",\"gmtModified\":null},\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        /*try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(responseString);*/

    }

    @Test
    public void getAllShopState() throws Exception {
        String responseString = this.mvc.perform(get("/shop/shops/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void updateShop() throws Exception {
        ShopVo vo = new ShopVo();
        vo.setName("test");
        String shopJson = JacksonUtil.toJson(vo);

        String responseString = this.mvc.perform(
                put("/shop/shops/1")
                        .contentType("application/json;charset=UTF-8").content(shopJson)
        ).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(shopPoMapper.selectByPrimaryKey((long) 1).getName());
    }

    @Test
    public void shopOnShelves() throws Exception {

        String responseString = this.mvc.perform(
                put("/shop/shops/1/onshelves")
                        .contentType("application/json;charset=UTF-8")
        ).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void shopOffShelves() throws Exception {

        String responseString = this.mvc.perform(
                put("/shop/shops/2/offshelves")
                        .contentType("application/json;charset=UTF-8")
        ).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void deleteShop() throws Exception {

        String responseString = this.mvc.perform(
                delete("/shop/shops/9")
                        .contentType("application/json;charset=UTF-8")
        ).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 管理员审核店铺
     */
    @Test
    public void auditShop() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"2\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/shop/shops/0/newshops/2/audit")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(2L);
        System.out.println(shopPo.getState());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void auditShopPass() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"2\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/shop/shops/0/newshops/1/audit")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(1L);
        System.out.println(shopPo.getState());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /*         公开测试用例 */

    @Test
    public void getShopAllStates() throws Exception {
        byte[] ret = mallClient.get()
                .uri("/shop/shops/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"未审核\",\"code\":0},{\"name\":\"未上线\",\"code\":1},{\"name\":\"上线\",\"code\":2},{\"name\":\"关闭\",\"code\":3},{\"name\":\"审核未通过\",\"code\":4}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 测试新建商店
     *
     *
     * @throws Exception
     */

    @Test
    public void createShop2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"name\":\"星巴克\"}";
        byte[] ret = mallClient.post()
                .uri("/shop/shops")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("908")
                .jsonPath("$.errmsg").isEqualTo("用户已经有店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":908,\"errmsg\":\"用户已经有店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeShop1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"name\":\"麦当劳\"}";
        byte[] ret = mallClient.put()
                .uri("/shop/shops/1")
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
    public void changeShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"name\":\"麦当劳\"}";
        byte[] ret = mallClient.put()
                .uri("/shop/shops/10")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("504")
                .jsonPath("$.errmsg").isEqualTo("操作的资源id不存在")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void closeShop1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/shop/shops/1")
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
    public void closeShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/shop/shops/10")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("504")
                .jsonPath("$.errmsg").isEqualTo("操作的资源id不存在")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");String expectedResponse = "{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void closeShop4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.delete()
                .uri("/shop/shops/3")
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
    public void auditShop1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = mallClient.put()
                .uri("/shop/shops/0/newshops/1/audit")
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
    public void auditShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = mallClient.put()
                .uri("/shop/shops/0/newshops/2/audit")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("927")
                .jsonPath("$.errmsg").isEqualTo("当前店铺无法审批")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":927,\"errmsg\":\"当前店铺无法审批\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void auditShop4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = mallClient.put()
                .uri("/shop/shops/0/newshops/10/audit")
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
    public void onlineShop1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.put()
                .uri("/shop/shops/3/onshelves")
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
    public void onlineShop2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.put()
                .uri("/shop/shops/10/onshelves")
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
    public void onlineShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.put()
                .uri("/shop/shops/1/onshelves")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("504")
                .jsonPath("$.errmsg").isEqualTo("当前店铺无法上线")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":928,\"errmsg\":\"当前店铺无法上线\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }



    @Test
    public void offlineShop1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.put()
                .uri("/shop/shops/4/offshelves")
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
    public void offlineShop2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.put()
                .uri("/shop/shops/10/offshelves")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("504")
                .jsonPath("$.errmsg").isEqualTo("操作的资源id不存在")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void offlineShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = mallClient.put()
                .uri("/shop/shops/1/offshelves")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("929")
                .jsonPath("$.errmsg").isEqualTo("当前店铺无法下线")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":929,\"errmsg\":\"当前店铺无法下线\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
