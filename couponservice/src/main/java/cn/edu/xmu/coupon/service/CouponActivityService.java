package cn.edu.xmu.coupon.service;

import cn.edu.xmu.coupon.dao.CouponActivityDao;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.bo.CouponSku;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.vo.CouponActivityModifyVo;
import cn.edu.xmu.coupon.model.vo.CouponActivitySkuInputVo;
import cn.edu.xmu.ininterface.service.DisableCouponActivityService;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author BiuBiuBiu
 */
@Service
public class CouponActivityService implements DisableCouponActivityService {

    @Autowired
    CouponActivityDao couponActivityDao;

    @Value("${couponservice.dav.username}")
    private String davUsername;

    @Value("${couponservice.dav.password}")
    private String davPassword;

    @Value("${couponservice.dav.baseUrl}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityService.class);

    public ReturnObject deleteCouponSkuById(Long id, Long shopId) {
        return couponActivityDao.deleteCouponSkuById(id, shopId);
    }

    public ReturnObject modifyCouponActivityByID(Long id, Long shopId, CouponActivityModifyVo couponActivityModifyVo) {
        return couponActivityDao.modifyCouponActivityByID(id, shopId, couponActivityModifyVo);
    }

    public ReturnObject rangeForCouponActivityById(Long id, Long shopId, CouponActivitySkuInputVo couponActivitySkuInputVo) {
        ReturnObject returnObject;
        CouponSkuPo couponSkuPo = couponActivityDao.rangeForCouponActivityById(id, shopId, couponActivitySkuInputVo);
        if (couponSkuPo != null) {
            returnObject = new ReturnObject(new CouponSku(couponSkuPo));
            logger.debug("rangeForCouponActivityById : " + returnObject);
        } else {
            logger.debug("rangeForCouponActivityById : fail!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;
    }

    /**
     * 上传优惠活动照片
     *
     * @param shopId
     * @param id
     * @param multipartFile
     * @return
     */
    @Transactional
    public ReturnObject uploadCouponActivityImg(Long shopId, Long id, MultipartFile multipartFile) {
        ReturnObject<CouponActivity> couponActivityReturnObject = couponActivityDao.getCouponActivityById(id);
        if (couponActivityReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
            logger.debug("uploadImg : failed");
            return couponActivityReturnObject;
        }
        Long shopid = couponActivityReturnObject.getData().getShop_id();
        if (shopid.equals(shopId)) {
            if (couponActivityReturnObject.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST) {
                return couponActivityReturnObject;
            }
            CouponActivity couponActivity = couponActivityReturnObject.getData();
            ReturnObject returnObject = new ReturnObject();
            try {
                returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);
                //文件上传错误
                if (returnObject.getCode() != ResponseCode.OK) {
                    logger.debug(returnObject.getErrmsg());
                    return returnObject;
                }
                String oldFilename = null;
                if (couponActivity.getImage_url() != null) {
                    int baseUrlIndex = couponActivity.getImage_url().lastIndexOf("/");
                    oldFilename = couponActivity.getImage_url().substring(baseUrlIndex + 1);
                }
                couponActivity.setImage_url(baseUrl + returnObject.getData().toString());
                ReturnObject updateReturnObject = couponActivityDao.updateGoodsSpuImgUrl(couponActivity);

                //数据库更新失败，需删除新增的图片
                if (updateReturnObject.getCode() == ResponseCode.FIELD_NOTVALID) {
                    ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                    return updateReturnObject;
                }

                //数据库更新成功需删除旧图片，未设置则不删除
                if (oldFilename != null) {
                    ImgHelper.deleteRemoteImg(oldFilename, davUsername, davPassword, baseUrl);
                }
            } catch (IOException e) {
                logger.debug("uploadImg: I/O Error:" + baseUrl);
                return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
            }
            return returnObject;
        } else {
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
    }

    public ReturnObject CouponActivityOnShelves(Long shopId,Long id)
    {
        return couponActivityDao.CouponActivityOnShelves(shopId, id);
    }

    public ReturnObject CouponActivityOffShelves(Long shopId,Long id)
    {
        return couponActivityDao.CouponActivityOffShelves(shopId, id);
    }

    public ReturnObject deleteCouponActivity(Long shopId,Long id){
        return couponActivityDao.deleteCouponActivity(shopId, id);
    }

    @Override
    public boolean disableActivity(Long shopId) {
        return couponActivityDao.disableActivity(shopId);
    }
}
