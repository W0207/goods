package cn.edu.xmu.goods.dao;


import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class ShopDao {

    @Autowired
    ShopPoMapper shopPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(ShopDao.class);


    public ShopPo findShopById(Long id)
    {
        return shopPoMapper.selectByPrimaryKey(id);
    }

    /**
     * 增加一个店铺
     * <p>
     * by 宇
     */
    public ReturnObject<Shop> insertShop(Shop shop) {
        ShopPo shopPo = shop.getShopPo();
        ReturnObject<Shop> returnObject = null;
        try {
            int ret = shopPoMapper.insertSelective(shopPo);
            if (ret == 0) {
                //插入失败
                returnObject = new ReturnObject<>(ResponseCode.USER_HASSHOP, String.format("店家id不存在" + shopPo.getName()));
            } else {
                //插入成功
                shop.setId(shopPo.getId());
                returnObject = new ReturnObject<>(shop);
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;
    }

    /**
     * 修改店铺信息
     * <p>
     * by 宇
     */
    public ReturnObject<Shop> updateShop(Shop shop) {
        ShopPo shopPo = shop.getShopPo();
        ReturnObject<Shop> returnObject = null;
        try {
            int ret = shopPoMapper.updateByPrimaryKeySelective(shopPo);
            System.out.printf("更新的返回值为：%d", ret);
            System.out.printf(shopPo.toString());
            if (ret == 0) {
                //修改失败
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺id不存在：" + shopPo.getId()));

            } else {
                //修改成功
                returnObject = new ReturnObject<>();
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;

    }

    /**
     * 上下线商店
     * by 宇
     */
    public ReturnObject<Shop> shopShelves(Shop shop) {
        ShopPo shopPo = shop.getShopPo();

        ReturnObject returnObject = null;
        try {
            ShopPo shopPoSelect = shopPoMapper.selectByPrimaryKey(shopPo.getId());

            if (shopPoSelect == null) {
                //店铺id不存在
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺id不存在：" + shopPo.getId()));
            } else {
                Shop shopSelect = new Shop(shopPoSelect);
                if (shopPo.getState().equals(Shop.State.CLOSE) || shopPo.getState().equals(Shop.State.UNPASS) || shopPo.getState().equals(Shop.State.NEW)) {
                    logger.info(shopSelect.getState().getDescription() + "当前状态无法进行变迁");
                    returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不允许转换" + shopPo.getId()));
                } else {
                    //可以修改状态
                    shopPoMapper.updateByPrimaryKeySelective(shopPo);
                    returnObject = new ReturnObject<>();
                }
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {

            // 其他Exception错误
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;
    }

    /**
     * 删除店家
     * by 宇
     */
    public ReturnObject<Shop> deleteShop(Shop shop) {
        ShopPo shopPo = shop.getShopPo();

        ReturnObject returnObject = null;
        try {
            ShopPo shopPoSelect = shopPoMapper.selectByPrimaryKey(shopPo.getId());
            if (shopPoSelect == null) {
                //店铺id不存在
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺id不存在：" + shopPo.getId()));
            } else {
                System.out.println(shopPoSelect.toString());
                Shop shopSelect = new Shop(shopPoSelect);
                if (shopPo.getState().equals(Shop.State.NEW) || shopPo.getState().equals(Shop.State.UNPASS)) {
                    logger.info(shopSelect.getState().getDescription() + "对店家id进行了物理删除");
                    int ret = shopPoMapper.deleteByPrimaryKey(shopPo.getId());
                } else {
                    logger.info(shopSelect.getState().getDescription() + "对店家id进行了逻辑删除");
                    shopPoMapper.updateByPrimaryKeySelective(shopPo);
                    returnObject = new ReturnObject<>();
                }
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;

    }

}
