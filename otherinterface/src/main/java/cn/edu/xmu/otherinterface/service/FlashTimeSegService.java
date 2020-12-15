package cn.edu.xmu.otherinterface.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.bo.MyReturn;

public interface FlashTimeSegService {

    /**
     * 删除秒杀时间段后应当调用商品Api将相应的segId置为空值
     *
     * @param segId 时间段id
     * @return 是否全部置为空值，请注意校验个数
     */
    MyReturn<Boolean> delFlashTimeSeg(Long segId);
}
