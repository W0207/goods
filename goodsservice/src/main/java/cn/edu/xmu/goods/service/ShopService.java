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

    public ReturnObject insertShop(Shop shop){
        ReturnObject<Shop> returnObject = shopDao.insertShop(shop);
        return returnObject;
    }

    public ReturnObject updateShop(Shop shop){
        ReturnObject<Shop> returnObject = shopDao.updateShop(shop);
        return  returnObject;
    }
}
