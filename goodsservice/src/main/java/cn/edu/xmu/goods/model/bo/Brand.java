package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

@Data
public class Brand implements VoObject {

    private Long id;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
