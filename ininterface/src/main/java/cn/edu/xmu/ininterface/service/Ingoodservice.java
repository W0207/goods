package cn.edu.xmu.ininterface.service;

import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;

/**
 * @author ASUS
 */
public interface Ingoodservice {

    SkuToPresaleVo presaleFindSku(Long id);
}
