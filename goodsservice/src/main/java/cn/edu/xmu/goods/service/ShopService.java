package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {

    @Autowired
    ShopDao shopDao;

    //新建商家
    public ReturnObject insertShop(Shop shop) {
        ReturnObject<Shop> returnObject = shopDao.insertShop(shop);
        return returnObject;
    }

    //更新商家信息
    public ReturnObject updateShop(Shop shop) {
        ReturnObject<Shop> returnObject = shopDao.updateShop(shop);
        return returnObject;
    }

    //上线商家
    public ReturnObject shopOnShelves(Shop shop) {
        ReturnObject<Shop> returnObject = shopDao.shopShelves(shop);
        return returnObject;
    }

    //下线商家
    public ReturnObject shopOffShelves(Shop shop) {
        ReturnObject<Shop> returnObject = shopDao.shopShelves(shop);
        return returnObject;
    }

    //删除商家
    public ReturnObject deleteShop(Shop shop) {
        ReturnObject<Shop> returnObject = shopDao.deleteShop(shop);
        return returnObject;
    }
}
