package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GoodsSkuPoMapper;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDao {

    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    /**
     * @param id
     * @return GoodsSkuPo
     * @author shibin zhan
     */
    public GoodsSkuPo findGoodsSkuById(Long id) {
        GoodsSkuPoExample goodsSkuPoExample = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria = goodsSkuPoExample.createCriteria();
        criteria.andIdEqualTo(id);
        logger.debug("findGoodsSkuById: id=" + id);
        GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
        return goodsSkuPo;
    }
}
