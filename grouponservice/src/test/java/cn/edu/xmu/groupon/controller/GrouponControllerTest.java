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

}
