package cn.edu.xmu.presale.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.otherinterface.bo.MyReturn;
import cn.edu.xmu.presale.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OutDao {

    @Autowired
    PresaleActivityPoMapper presaleActivityPoMapper;

    public MyReturn<Long> getAdvancePrice(Long presaleId) {
        PresaleActivityPo presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(presaleId);
        if(presaleActivityPo==null){
            return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return new MyReturn<>(presaleActivityPo.getAdvancePayPrice());
    }

    public MyReturn<Long> getRestPrice(Long presaleId) {
        PresaleActivityPo presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(presaleId);
        if(presaleActivityPo==null){
            return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return new MyReturn<>(presaleActivityPo.getAdvancePayPrice());
    }
}
