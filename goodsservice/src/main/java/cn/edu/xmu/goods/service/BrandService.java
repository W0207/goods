package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public ReturnObject<PageInfo<VoObject>> findAllBrand(Integer page, Integer pageSize) {
        return brandDao.findAllBrand(page, pageSize);
    }
}
