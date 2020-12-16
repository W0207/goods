package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopAuditVo;
import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DubboService(version = "0.0.1")
public class ShopService implements InShopService {

    @Autowired
    ShopDao shopDao;

    @Override
    public ShopToAllVo presaleFindShop(Long id) {
        ShopPo shopPo = shopDao.findShopById(id);
        ShopToAllVo shopToAllVo = new ShopToAllVo();
        if (shopPo == null) {
            return null;
        } else {
            shopToAllVo.setId(id);
            shopToAllVo.setName(shopPo.getName());
            return shopToAllVo;
        }
    }

    /**
     * 判断店铺是否存在
     *
     * @param shopId
     * @return
     */
    @Override
    public boolean shopExitOrNot(Long shopId) {
        ShopPo shopPo = shopDao.findShopById(shopId);
        return shopPo != null;
    }

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
        ReturnObject returnObject = shopDao.shopShelves(shop);
        return returnObject;
    }

    //下线商家
    public ReturnObject shopOffShelves(Shop shop) {
        ReturnObject returnObject = shopDao.shopShelves(shop);
        return returnObject;
    }

    //删除商家
    public ReturnObject deleteShop(Shop shop) {
        ReturnObject<Shop> returnObject = shopDao.deleteShop(shop);
        return returnObject;
    }

    public ReturnObject auditShopByID(Long id, ShopAuditVo shopAuditVo) {
        return shopDao.auditShopByID(id, shopAuditVo);
    }
}
