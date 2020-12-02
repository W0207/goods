package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.mapper.BrandPoMapper;
import cn.edu.xmu.goods.mapper.FloatPricePoMapper;
import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.bo.FloatPrice;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class GoodsControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;

    @Autowired
    FloatPricePoMapper floatPricePoMapper;

    @Autowired
    BrandPoMapper brandPoMapper;

    /**
     * 获得商品spu所有状态
     */
    @Test
    public void getAllSpuState() throws Exception {
        String responseString = this.mvc.perform(get("/goods/spus/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未上架\", \"code\": 0 }, { \"name\": \"上架\", \"code\": 4 }, { \"name\": \"已删除\", \"code\": 6 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 获得商品sku的详细信息
     */
    @Test
    public void findGoodsSkuById() throws Exception {
        String responseString = this.mvc.perform(get("/goods/skus/273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 获得一条商品spu的详细信息
     */
    @Test
    public void findGoodsSpuById() throws Exception {
        String responseString = this.mvc.perform(get("/goods/spus/273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 修改商品spu信息
     *
     * @throws Exception
     */
    @Test
    public void changeSpuInfoById() throws Exception {
        String requireJson = "{\n  \"name\":\"123\",\n  \"description\":\"123\",\n  \"specs\": \"123\"\n}";
        String responseString = this.mvc.perform(put("/goods/shops/0/spus/273")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 删除商品
     *
     * @throws Exception
     */
    @Test
    public void deleteGoodsSpu() throws Exception {
        String responseString = this.mvc.perform(delete("/goods/shops/0/spus/273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getState());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 下架商品
     *
     * @throws Exception
     */
    @Test
    public void putOffGoodsSpuOnSales() throws Exception {
        String responseString = this.mvc.perform(put("/goods/shops/0/spus/273/offshelves"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getState());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 上架商品之后重新上架
     *
     * @throws Exception
     */
    @Test
    public void putGoodsSpuOnSales() throws Exception {
        String responseString = this.mvc.perform(put("/goods/shops/0/spus/273/offshelves"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getState());

        responseString = this.mvc.perform(put("/goods/shops/0/spus/273/onshelves"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getState());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 店家或管理员删除sku
     *
     * @throws Exception
     */
    @Test
    public void deleteGoodsSku() throws Exception {
        String responseString = this.mvc.perform(delete("/goods/shops/0/skus/273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getDisabled());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /**
     * 获得所有品牌
     */
    @Test
    public void getAllBrand() throws Exception {
        String responseString = this.mvc.perform(get("/goods/brands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errormessage\":成功,\"data\":{\"total\":18,\"pages\":2,\"pageSize\":10,\"page\":1,\"list\":[{\"id\":2,\"name\":\"查看任意用户信息\",\"imageUrl\":\"123\",\"detail\":0,\"gmtCreate\":\"2020-11-01T09:52:20\",\"gmtModified\":\"2020-11-02T21:51:45\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * <<<<<<< HEAD
     * 修改商品sku信息
     *
     * @throws Exception
     */
    @Test
    public void changeSkuInfoById() throws Exception {
        String requireJson = "{\n" +
                "  \"name\":\"123\",\n" +
                "  \"originalPrice\":\"123\",\n" +
                "  \"configuration\": \"123\",\n" +
                "  \"weight\":\"123\",\n" +
                "  \"inventory\":\"123\",\n" +
                "  \"detail\":\"123\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/goods/shops/0/skus/273")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(273L);
        System.out.println(goodsSkuPo.getConfiguration());
        System.out.println(goodsSkuPo.getDetail());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getSkuList() throws Exception {
        String responseString = this.mvc.perform(get("/goods/sku"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 管理员失效商品价格浮动
     *
     * @throws Exception
     */
    @Test
    public void invalidFloatPrice() throws Exception {
        System.out.println(floatPricePoMapper.selectByPrimaryKey(273L).getValid());
        String responseString = this.mvc.perform(delete("/goods/shops/0/floatprice/273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(floatPricePoMapper.selectByPrimaryKey(273L).getValid());
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 修改品牌信息
     */
    @Test
    public void modifyBrand() throws Exception {
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getName());
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getDetail());
        String requireJson = "{\n" +
                "  \"name\":\"123\",\n" +
                "  \"detail\":\"123\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/goods/shops/0/brands/71")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getName());
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getDetail());
    }

    /**
     * 管理员删除品牌
     *
     * @throws Exception
     */
    @Test
    public void deleteBrand() throws Exception {
        getAllBrand();
        String responseString = this.mvc.perform(delete("/goods/shops/0/brands/72"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        getAllBrand();
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
