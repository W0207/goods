package cn.edu.xmu.presale.model.vo;

import cn.edu.xmu.presale.model.bo.PresaleActivity;
import lombok.Data;

@Data
public class PresaleActivityStateVo {
    private Long code;

    private String name;

    public PresaleActivityStateVo(PresaleActivity.State state) {
        code = Long.valueOf(state.getCode());
        name = state.getDescription();
    }

}
