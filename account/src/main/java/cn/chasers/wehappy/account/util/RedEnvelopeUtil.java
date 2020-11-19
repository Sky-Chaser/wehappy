package cn.chasers.wehappy.account.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 红包工具类
 *
 * @author zhangyuanhang
 * @date 2020/11/19: 9:10 下午
 */
@Slf4j
public class RedEnvelopeUtil {
    private final static BigDecimal MIN = new BigDecimal("0.01");

    /**
     * 二倍均值算法，计算红包金额
     *
     * @param count  剩余份数
     * @param amount 总金额
     * @return 返回下一个红包金额
     */
    public static BigDecimal doubleAverage(int count, BigDecimal amount) {
        if (count == 1) {
            return amount;
        }

        BigDecimal max = amount.subtract(MIN.multiply(BigDecimal.valueOf(count - 1)));
        BigDecimal avg = max.divide(BigDecimal.valueOf(count), 2, RoundingMode.UP).multiply(BigDecimal.valueOf(2));
        return RandomUtil.randomBigDecimal(MIN, avg.min(max)).round(new MathContext(2, RoundingMode.DOWN));
    }

    public static void main(String[] args) {
        BigDecimal amount = BigDecimal.valueOf(100);

        for (int i = 10; i > 0; i--) {
            BigDecimal redEnvelope = doubleAverage(i, amount);
            System.out.println(redEnvelope);
            amount = amount.subtract(redEnvelope);
        }
    }
}
