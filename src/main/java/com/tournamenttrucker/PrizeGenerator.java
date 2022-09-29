package com.tournamenttrucker;

import java.util.HashMap;
import java.util.Map;

public class PrizeGenerator {
    private static Map<Integer, PrizePercentageDistribution> prizeOptions = new HashMap<Integer, PrizePercentageDistribution>(){
        {
            put(1, new PrizePercentageDistribution(100,0));
            put(2, new PrizePercentageDistribution(90, 10));
            put(3, new PrizePercentageDistribution(80, 20));
            put(4, new PrizePercentageDistribution(70, 30));
            put(5, new PrizePercentageDistribution(60, 40));
        }
    };

    public static Map<Integer, PrizePercentageDistribution> getPrizeOptions() {
        return prizeOptions;
    }

    public static Map<Integer, PrizePercentageDistribution> Generate(){
        return prizeOptions;
    }

    public static PrizePercentageDistribution getByOption(int optionNumber){
        return prizeOptions.get(optionNumber);
    }
}
