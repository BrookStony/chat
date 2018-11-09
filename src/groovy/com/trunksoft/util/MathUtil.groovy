package com.trunksoft.util

import java.text.DecimalFormat

class MathUtil {

    private static final DecimalFormat DEF = new DecimalFormat("0.00")

    static double format(long size){
        def s = size as double
        return Double.valueOf(DEF.format(s/1024)).doubleValue()
    }
}
