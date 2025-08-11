package org.ironlegion.Util;

import java.math.BigDecimal;

public class SkillLevelCalculator {

    // Cumulative XP to reach each level (index = level)
    private static final double[] CUMULATIVE_XP = {
            0, 50, 175, 375, 675, 1175, 1925, 2925, 4425, 6425,
            9925, 14925, 22425, 32425, 47425, 67425, 97425, 147425, 222425, 322425,
            522425, 822425, 1222425, 1722425, 2322425, 3022425, 3822425, 4722425, 5722425, 6822425,
            8022425, 9322425, 10722425, 12222425, 13822425, 15522425, 17322425, 19222425, 21222425, 23322425,
            25522425, 27822425, 30222425, 32722425, 35322425, 38072425, 40972425, 44072425, 47472425, 51172425,
            55172425, 59472425, 64072425, 68972425, 74172425, 79672425, 85472425, 91572425, 97972425, 104972425,
            111672425
    };

    public static double xpToLevel(BigDecimal xp) {
        double xpValue = xp.doubleValue();

        for (int level = 0; level < CUMULATIVE_XP.length - 1; level++) {
            double currentXP = CUMULATIVE_XP[level];
            double nextXP = CUMULATIVE_XP[level + 1];

            if (xpValue < nextXP) {
                double progress = (xpValue - currentXP) / (nextXP - currentXP);
                return level + progress;
            }
        }
        return CUMULATIVE_XP.length - 1; // Max level if XP exceeds table
    }

}