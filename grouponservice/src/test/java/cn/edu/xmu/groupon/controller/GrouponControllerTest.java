package cn.edu.xmu.groupon.controller;

import cn.edu.xmu.groupon.GrouponServiceApplication;
import cn.edu.xmu.groupon.controller.GrouponController;
import cn.edu.xmu.groupon.mapper.GrouponActivityPoMapper;
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


    /**
     * 获得团购活动的所有状态
     */
    @Test
    public void getgrouponState () throws Exception {
        String responseString = this.mvc.perform(get("/groupon/groupons/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"已新建\", \"code\": 0 }, { \"name\": \"已结束\", \"code\": 6 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 查看所有的团购活动
     */
    @Test
    public void queryGroupons() throws Exception {
        String responseString = this.mvc.perform(get("/groupon/groupons"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 查看店铺所有团购活动
     */
    @Test
    public void queryGroupon() throws Exception {
        String responseString = this.mvc.perform(get("/groupon/shops/1/groupons"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 查询SPU团购活动
     *
     * @throws Exception
     */
    @Test
    public void queryGrouponofSPU() throws Exception {
        String responseString = this.mvc.perform(get("/groupon/shops/0/spus/273/groupons"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
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
        String responseString = this.mvc.perform(post("/groupon/shops/0/spus/273/groupons")
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
        String responseString = this.mvc.perform(put("/groupon/shops/0/groupons/72")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(grouponActivityPoMapper.selectByPrimaryKey(72L).getStrategy());
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

}
