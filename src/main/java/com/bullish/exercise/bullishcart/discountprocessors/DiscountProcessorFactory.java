package com.bullish.exercise.bullishcart.discountprocessors;

import com.bullish.exercise.bullishcart.util.DiscountRuleEnum;

public class DiscountProcessorFactory {
    public static DiscountProcessor getDiscountProcessor(DiscountRuleEnum discountRule){
        if (discountRule ==DiscountRuleEnum.PERCENT_DISCOUNT){
            return new PercentPerItemDiscountProcessor();
        }
        if (discountRule == DiscountRuleEnum.DOLLAR_DISCOUNT){
            return new FlatRatePerItemDiscountProcessor();
        }
        return null;
    }

}
