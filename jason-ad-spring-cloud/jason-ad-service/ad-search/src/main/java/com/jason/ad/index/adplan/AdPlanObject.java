package com.jason.ad.index.adplan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//adplanobject是作为一个索引对象存在, 我们会把数据表翻译成一个索引对象
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;


    public void update(AdPlanObject newObject){
        if(newObject.getPlanId() != null){
            this.planId = newObject.planId;
        }
        if(newObject.getUserId() != null){
            this.userId = newObject.userId;
        }
        if(newObject.getPlanStatus() != null){
            this.planStatus = newObject.planStatus;
        }
        if(newObject.getStartDate() != null){
            this.startDate = newObject.startDate;
        }
        if(newObject.getEndDate() != null){
            this.endDate = newObject.endDate;
        }
    }
}
