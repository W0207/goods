package cn.edu.xmu.goods.dao;


import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class ShopDao {

    @Autowired
    ShopPoMapper shopPoMapper;

    /**
     * 增加一个店铺
     *
     * by 宇
     */
    public ReturnObject<Shop> insertShop(Shop shop)
    {
        ShopPo shopPo = shop.getShopPo();
        ReturnObject<Shop> returnObject = null;
        try {
            int ret = shopPoMapper.insertSelective(shopPo);
            if(ret == 0){
                //插入失败
                returnObject = new ReturnObject<>(ResponseCode.USER_HASSHOP,String.format("新增失败：" + shopPo.getName()));
            }
            else {
                //插入成功
                shop.setId(shopPo.getId());
                returnObject = new ReturnObject<>(shop);
            }
        }
        catch (DataAccessException e)
        {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e){
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;
    }

}
