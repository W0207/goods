package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.controller.GoodsController;
import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
@Slf4j
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

    @Autowired
    GoodsCategoryPoMapper goodsCategoryPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }
    private WebTestClient mallClient;
    private WebTestClient manageClient;

    public GoodsControllerTest() {
        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        /*this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://"+managementGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();*/
    }
    /**
     * 获得商品sku所有状态
     */
    @Test
    public void getAllSkuState() throws Exception {
        String responseString = this.mvc.perform(get("/goods/skus/states"))
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
        String token1 = creatTestToken(1L, 0L, 100);
        String requireJson1 = "{\n" +
                "  \"activityPrice\": 222,\n" +
                "  \"beginTime\": \"2020-12-04 11:24:47\",\n" +
                "  \"endTime\": \"2020-12-22 11:24:49\",\n" +
                "  \"quantity\": 1\n" +
                "}";
        String responseString1 = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token1)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson1))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString1);

        //登陆
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(get("/goods/skus/273")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);

        //不登陆
//        String responseString = this.mvc.perform(get("/goods/skus/273"))
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        System.out.println(responseString);
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
     * 删除商品spu
     *
     * @throws Exception
     */
    @Test
    public void deleteGoodsSpu() throws Exception {
        String token = creatTestToken(1L, 1L, 100);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L));
        String responseString = this.mvc.perform(delete("/goods/shops/0/spus/273")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(291L));
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    //下架商品测试开始

    /**
     * 下架商品(token正确，单是无权限修改不属于自己店铺的东西)
     *
     * @throws Exception
     */
    @Test
    public void putOffGoodsSpuOnSales1() throws Exception {
        String token = creatTestToken(1L, 1L, 100);
        String responseString = this.mvc.perform(put("/goods/shops/1/skus/273/offshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
    }

    /**
     * 下架商品(下架成功)
     *
     * @throws Exception
     */
    @Test
    public void putOffGoodsSpuOnSales2() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/goods/shops/0/skus/273/offshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
    }

    /**
     * 下架商品(token不对)
     *
     * @throws Exception
     */
    @Test
    public void putOffGoodsSpuOnSales3() throws Exception {
        String token = creatTestToken(1L, 1L, 100);
        String responseString = this.mvc.perform(put("/goods/shops/0/skus/273/offshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
    }

    /**
     * 下架商品(平台管理员修改，但是对应店铺id不对)
     *
     * @throws Exception
     */
    @Test
    public void putOffGoodsSpuOnSales4() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/goods/shops/0/skus/273/offshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
    }
    //下架商品测试结束

    /**
     * 下架商品之后重新上架
     *
     * @throws Exception
     */
    @Test
    public void putGoodsSpuOnSales() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/goods/shops/0/skus/273/offshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());

        responseString = this.mvc.perform(put("/goods/shops/0/skus/273/onshelves")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
        //.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 店家或管理员删除sku
     *
     * @throws Exception
     */
    @Test
    public void deleteGoodsSku() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
        String responseString = this.mvc.perform(delete("/goods/shops/0/skus/273")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getState());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
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
     * 修改商品sku信息
     *
     * @throws Exception
     */
    @Test
    public void changeSkuInfoById() throws Exception {
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(273L);
        System.out.println(goodsSkuPo.getInventory());
        String requireJson = "{\n" +
                "  \"name\":\"123\",\n" +
                "  \"originalPrice\":\"123\",\n" +
                "  \"configuration\": \"123\",\n" +
                "  \"weight\":\"123\",\n" +
                "  \"inventory\":\"123\",\n" +
                "  \"detail\":\"123\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/goods/shops/0/skus/273")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(273L);
        System.out.println(goodsSkuPo.getInventory());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test//spuId和spuSn冲突
    public void getSkuList() throws Exception {
        String responseString = this.mvc.perform(get("/goods/sku?page=1&pageSize=10&spuId=274&spuSn=drh-d0001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test //通过查询 spuId和spuSn无冲突
    public void getSkuList1() throws Exception {
        String responseString = this.mvc.perform(get("/goods/sku?page=1&pageSize=10&spuId=273&spuSn=drh-d0001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test //通过查询 spuId和spuSn无冲突
    public void getSkuList2() throws Exception {
        String responseString = this.mvc.perform(get("/goods/sku?page=1&pageSize=10&spuId=273&spuSn=drh-d0001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test //spuId和shopId冲突
    public void getSkuList3() throws Exception {
        String responseString = this.mvc.perform(get("/goods/sku?page=1&pageSize=10&spuId=273&shopId=1"))
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
        String token = creatTestToken(9999L, 0L, 100);
        //System.out.println(floatPricePoMapper.selectByPrimaryKey(1L).getValid());
        String responseString = this.mvc.perform(delete("/goods/shops/0/floatprice/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(floatPricePoMapper.selectByPrimaryKey(1L).getInvalidBy());
        System.out.println(floatPricePoMapper.selectByPrimaryKey(1L).getValid());

        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 修改品牌信息
     */
    @Test
    public void modifyBrand() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getName());
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getDetail());
        String requireJson = "{\n" +
                "  \"name\":\"123\",\n" +
                "  \"detail\":\"123\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/goods/shops/0/brands/71")
                .header("authorization", token)
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
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(delete("/goods/shops/0/brands/71")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        getAllBrand();
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员新增商品类目
     *
     * @throws Exception
     */
    @Test
    public void addCategory() throws Exception {
        String requireJson = "{\n  \"name\":\"家电\"}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(post("/goods/shops/0/categories/139/subcategories")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        // JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员修改商品类目信息
     *
     * @throws Exception
     */
    @Test
    public void modifyGoodsType() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());
        String requireJson = "{\n  \"name\":\"123\"}";
        String responseString = this.mvc.perform(put("/goods/shops/0/categories/122")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L).getName());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员删除商品类目信息
     *
     * @throws Exception
     */
    @Test
    public void delCategory() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(delete("/goods/shops/0/categories/121")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        System.out.println(goodsCategoryPoMapper.selectByPrimaryKey(122L));// 查询测试是否删除成功
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * spu添加品牌
     *
     * @throws Exception
     */
    @Test
    public void spuAddBrand() throws Exception {
        String token = creatTestToken(1111L, 0L, 100);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getBrandId());
        String responseString = this.mvc.perform(post("/goods/shops/0/spus/273/brands/70")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getBrandId());
    }

    /**
     * 商品分类信息
     *
     * @throws Exception
     */
    @Test
    public void queryType() throws Exception {
        String responseString = this.mvc.perform(get("/goods/categories/199/subcategories"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * sku上传照片
     */
    @Test
    public void uploadSkuImage() throws Exception {
        String token = creatTestToken(1111L, 0L, 100);
        File file = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "img" + File.separator + "timg.png");
        MockMultipartFile firstFile = new MockMultipartFile("img", "timg.png", "multipart/form-data", new FileInputStream(file));
        String responseString = mvc.perform(MockMvcRequestBuilders
                .multipart("/goods/shops/0/skus/273/uploadImg")
                .file(firstFile)
                .header("authorization", token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(goodsSkuPoMapper.selectByPrimaryKey(273L).getImageUrl());
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * spu上传照片
     */
    @Test
    public void uploadSpuImage() throws Exception {
        String token = creatTestToken(1111L, 1L, 100);
        File file = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "img" + File.separator + "timg.png");
        MockMultipartFile firstFile = new MockMultipartFile("img", "timg.png", "multipart/form-data", new FileInputStream(file));
        String responseString = mvc.perform(MockMvcRequestBuilders
                .multipart("/goods/shops/1/spus/273/uploadImg")
                .file(firstFile)
                .header("authorization", token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 品牌上传照片
     */
    @Test
    public void uploadBrandImage() throws Exception {
        String token = creatTestToken(1111L, 0L, 100);
        File file = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "img" + File.separator + "timg.png");
        MockMultipartFile firstFile = new MockMultipartFile("img", "timg.png", "multipart/form-data", new FileInputStream(file));
        String responseString = mvc.perform(MockMvcRequestBuilders
                .multipart("/goods/shops/1/brands/71/uploadImg")
                .file(firstFile)
                .header("authorization", token)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(brandPoMapper.selectByPrimaryKey(71L).getImageUrl());
        System.out.println(responseString);
    }

    /**
     * 商品加入分类
     *
     * @throws Exception
     */
    @Test
    public void spuAddCategories() throws Exception {
        String token = creatTestToken(1111L, 0L, 100);
        String responseString = this.mvc.perform(post("/goods/shops/0/spus/277/categories/126")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(277L).getCategoryId());
    }

    /**
     * spu删除分类
     *
     * @throws Exception
     */
    @Test
    public void spuDeleteCategories() throws Exception {
        String token = creatTestToken(1111L, 0L, 100);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(277L).getCategoryId());
        String responseString = this.mvc.perform(delete("/goods/shops/0/spus/277/categories/127")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(277L).getCategoryId());
    }

    /**
     * spu删除品牌
     *
     * @throws Exception
     */
    @Test
    public void spuDeleteBrand() throws Exception {
        String token = creatTestToken(1111L, 0L, 100);
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getBrandId());
        String responseString = this.mvc.perform(delete("/goods/shops/0/spus/273/brands/71")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(goodsSpuPoMapper.selectByPrimaryKey(273L).getBrandId());
        System.out.println(responseString);
    }

    //管理员新增商品价格浮动测试 开始

    /**
     * 开始时间大于结束时间
     *
     * @throws Exception
     */
    @Test
    public void addFloatingPrice1() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"activityPrice\": 0,\n" +
                "  \"beginTime\": \"2020-12-07 11:24:47\",\n" +
                "  \"endTime\": \"2020-12-07 11:24:46\",\n" +
                "  \"quantity\": 0\n" +
                "}";
        String responseString = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 无开始时间
     *
     * @throws Exception
     */
    @Test
    public void addFloatingPrice2() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"activityPrice\": 0,\n" +
                "  \"beginTime\": \"\",\n" +
                "  \"endTime\": \"2020-12-07 11:24:46\",\n" +
                "  \"quantity\": 0\n" +
                "}";
        String responseString = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 无结束时间
     *
     * @throws Exception
     */
    @Test
    public void addFloatingPrice3() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"activityPrice\": 0,\n" +
                "  \"beginTime\": \"2020-12-07 11:24:47\",\n" +
                "  \"endTime\": \"\",\n" +
                "  \"quantity\": 0\n" +
                "}";
        String responseString = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 设置的库存大于总库存
     *
     * @throws Exception
     */
    @Test
    public void addFloatingPrice4() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"activityPrice\": 0,\n" +
                "  \"beginTime\": \"2020-12-07 11:24:47\",\n" +
                "  \"endTime\": \"2020-12-07 11:24:49\",\n" +
                "  \"quantity\": 10\n" +
                "}";
        String responseString = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 正常添加和时间冲突
     *
     * @throws Exception
     */
    @Test
    public void addFloatingPrice5() throws Exception {
        String token1 = creatTestToken(1L, 0L, 100);
        String requireJson1 = "{\n" +
                "  \"activityPrice\": 0,\n" +
                "  \"beginTime\": \"2020-12-07 11:24:47\",\n" +
                "  \"endTime\": \"2020-12-07 11:24:49\",\n" +
                "  \"quantity\": 1\n" +
                "}";
        String responseString1 = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token1)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson1))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString1);

        String token2 = creatTestToken(1L, 0L, 100);
        String requireJson2 = "{\n" +
                "  \"activityPrice\": 0,\n" +
                "  \"beginTime\": \"2020-12-07 11:24:48\",\n" +
                "  \"endTime\": \"2020-12-07 11:24:49\",\n" +
                "  \"quantity\": 0\n" +
                "}";
        String responseString2 = this.mvc.perform(post("/goods/shops/0/skus/273/floatPrices")
                .header("authorization", token2)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson2))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString2);
    }
    //管理员新增商品价格浮动测试 结束

    /**
     * 管理员新增品牌
     *
     * @throws Exception
     */
    @Test
    public void addBrand() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"name\":\"123\",\n" +
                "  \"detail\":\"123\"\n" +
                "}";
        System.out.println(requireJson);
        String responseString = this.mvc.perform(post("/goods/shops/0/brands")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void addSpu() throws Exception {
        String token = creatTestToken(1L, 1L, 100);
        String requireJson = "{\n  \"name\":\"123\",\n  \"description\":\"123\",\n  \"specs\": \"123\"\n}";
        String responseString = this.mvc.perform(put("/goods/shops/1/spus")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 修改商品spu信息
     *
     * @throws Exception
     */
    @Test
    public void changeSpuInfoById() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n  \"name\":\"123\",\n  \"description\":\"123\",\n  \"specs\": \"123\"\n}";
        String responseString = this.mvc.perform(put("/goods/shops/0/spus/273")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 管理员添加新的SKU到SPU中
     *
     * @throws Exception
     */
    @Test
    public void createSku() throws Exception {
        String token = creatTestToken(1L, 0L, 100);
        String requireJson = "{\n" +
                "  \"sn\":\"123\",\n" +
                "  \"name\":\"123\",\n" +
                "  \"originalPrice\":\"123\",\n" +
                "  \"configuration\": \"123\",\n" +
                "  \"weight\":\"123\",\n" +
                "  \"imageUrl\":\"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "  \"inventory\":\"123\",\n" +
                "  \"detail\":\"123\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/goods/shops/0/spus/273/skus")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /*       公开测试用例

    @Test
    @Order(2)
    public void getAllSkus(){
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = mallClient.get().uri(getPath("/skus"));
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.total").isNumber()
                .jsonPath("$.data.pages").isNumber()
                .returnResult()
                .getResponseBodyContent();
    }

    //通过id获取sku
    @Test
    @Order(3)
    public void getkuById(){
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = mallClient.get().uri(getPath("/skus/300"));
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.name").isEqualTo("+")
                .jsonPath("$.data.spu.id").isEqualTo("300")
                .jsonPath("$.data.spu.name").isEqualTo("金和汇景•赵紫云•粉彩绣球瓷瓶")
                .returnResult()
                .getResponseBodyContent();
    }


    //添加sku到spu  已登录
    @Test
    @Order(4)
    public void addSkuToSpu1() throws Exception{
        byte[] responseBuffer = null;
        String token = login("13088admin", "123456");
        String bodyValue = "{\n" +
                "  \"sn\": \"string\",\n" +
                "  \"name\": \"测试商品\",\n" +
                "  \"originalPrice\": 100,\n" +
                "  \"configuration\": \"ddddd\",\n" +
                "  \"weight\": 10,\n" +
                "  \"imageUrl\": null,\n" +
                "  \"inventory\": 100,\n" +
                "  \"detail\": \"aaaaa\"\n" +
                "}";
        WebTestClient.RequestHeadersSpec res = manageClient.post().uri(getPath("/shops/0/spus/300/skus")).bodyValue(bodyValue).header("authorization",token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.name").isEqualTo("测试商品")
                .jsonPath("$.data.inventory").isEqualTo(100)
                .returnResult()
                .getResponseBodyContent();
        String response = new String(responseBuffer, "utf-8");
        ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        JsonNode node;
        Long id = null;
        try {
            node = mapper.readTree(response);
            JsonNode leaf = node.get("data");
            if (leaf != null) {
                JsonNode temp = leaf.get("id");
                if(temp != null){
                    id = temp.asLong();
                }
            }
        } catch (IOException e) {
            id =null;
        }

        WebTestClient.RequestHeadersSpec res2 = mallClient.get().uri(getPath("/skus/"+id.toString()));
        responseBuffer = res2.exchange().expectStatus().isNotFound().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }


    //添加sku到spu 未登录
    @Test
    @Order(5)
    public void addSkuToSpu2() throws Exception{
        byte[] responseBuffer = null;
        String bodyValue = "{\n" +
                "  \"sn\": \"string\",\n" +
                "  \"name\": \"测试商品\",\n" +
                "  \"originalPrice\": 100,\n" +
                "  \"configuration\": \"ddddd\",\n" +
                "  \"weight\": 10,\n" +
                "  \"imageUrl\": null,\n" +
                "  \"inventory\": 100,\n" +
                "  \"detail\": \"aaaaa\"\n" +
                "}";
        WebTestClient.RequestHeadersSpec res = manageClient.post().uri(getPath("/shops/0/spus/300/skus")).bodyValue(bodyValue);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AUTH_NEED_LOGIN.getCode())
                .returnResult()
                .getResponseBodyContent();
        String response = new String(responseBuffer, "utf-8");
    }

    //添加sku到spu 登录 但是操作的sku不存在
    @Test
    @Order(6)
    public void addSkuToSpu3() throws Exception{
        byte[] responseBuffer = null;
        String token = login("13088admin", "123456");
        String bodyValue = "{\n" +
                "  \"sn\": \"string\",\n" +
                "  \"name\": \"测试商品\",\n" +
                "  \"originalPrice\": 100,\n" +
                "  \"configuration\": \"ddddd\",\n" +
                "  \"weight\": 10,\n" +
                "  \"imageUrl\": null,\n" +
                "  \"inventory\": 100,\n" +
                "  \"detail\": \"aaaaa\"\n" +
                "}";
        WebTestClient.RequestHeadersSpec res = manageClient.post().uri(getPath("/shops/0/spus/9000/skus")).bodyValue(bodyValue).header("authorization",token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String response = new String(responseBuffer, "utf-8");
    }


    //正常删除 快乐路径
    @Test
    @Order(8)
    public void deleteSku() throws  Exception{
        byte[] responseBuffer = null;
        String token = login("13088admin", "123456");
        WebTestClient.RequestHeadersSpec res = manageClient.delete().uri(getPath("/shops/1/skus/8989")).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();

        WebTestClient.RequestHeadersSpec res2 = mallClient.get().uri(getPath("/skus/8989"));
        responseBuffer = res2.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
    }

    //删除的sku不属于自己的商铺
    @Test
    @Order(7)
    public void deleteSku2() throws Exception{
        byte[] responseBuffer = null;
        String token = login("13088admin", "123456");
        WebTestClient.RequestHeadersSpec res = manageClient.delete().uri(getPath("/shops/1/skus/300")).header("authorization", token);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
*/
}
