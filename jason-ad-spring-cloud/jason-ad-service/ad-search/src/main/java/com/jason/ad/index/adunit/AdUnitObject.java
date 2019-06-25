package com.jason.ad.index.adunit;

import com.jason.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;//与之关联的推广计划的索引

    void update(AdUnitObject newObject){
        if(newObject.getUnitId() != null){
            this.unitId = newObject.unitId;
        }
        if(newObject.getUnitStatus() != null){
            this.unitStatus = newObject.unitStatus;
        }
        if(newObject.getPositionType() != null){
            this.positionType = newObject.positionType;
        }
        if(newObject.getPlanId() != null){
            this.planId = newObject.planId;
        }
        if(newObject.getAdPlanObject() != null){
            this.adPlanObject = newObject.adPlanObject;
        }
    }

    private static boolean isKaiPing(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.KAIPING) > 0;
    }

    private static boolean isTiePian(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN) > 0;
    }

    private static boolean isTiePianMiddle(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN_MIDDLE) > 0;
    }

    private static boolean isTiePianPause(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN_PAUSE) > 0;
    }

    private static boolean isTiePianPost(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.TIEPIAN_POST) > 0;
    }

    //广告的type和这个unit的type是否匹配 就是要看是否一致!
    public static boolean isAdSlotTypeOk(int adSlotType, int positionType){
        switch (adSlotType){
            case AdUnitConstants.POSITION_TYPE.KAIPING:
                return isKaiPing(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN:
                return isTiePian(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN_MIDDLE:
                return isTiePianMiddle(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN_PAUSE:
                return isTiePianPause(positionType);
            case AdUnitConstants.POSITION_TYPE.TIEPIAN_POST:
                return isTiePianPost(positionType);
            default:
                    return false;
        }
    }
}
