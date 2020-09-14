package com.py7hon.threshold.scheme.shamir;

import com.py7hon.threshold.scheme.ThresholdScheme;

import java.util.LinkedList;
import java.util.Random;

/**
 * Shamir(k, n) 门限方案
 *
 * @author Seven
 * @version 1.0
 * @date 2020/9/1 11:19
 */
public class Shamir implements ThresholdScheme {

    // region ============= 生成密码片 =============

    @Override
    public long[] genPieces(long secretKey, int totalPieceNumber, int minEffectiveSliceNumber, long mod) {
        // 1. 随机生成多项式的系数。多项式的项数为：totalPieceNumber - minEffectiveSliceNumber
        // 系数，下标 0 表示 x^1 的系数，下标 n 表示 x^(n + 1) 的系数
        long[] coefficients = new long[totalPieceNumber - minEffectiveSliceNumber];
        // 生成随机数，取值范围为 [0, mod)
        genRandomNum(coefficients, mod);

        // 生成多项式为 f(x) = secretKey + coefficients[0] * x + coefficients[1] * x ^ 2 + ... + coefficients[n] * x ^ n
        // 2. 求每个块，即求 f(1), f(2) ... f(totalPieceNumber)
        long[] result = new long[totalPieceNumber];
        for (int i = 0; i < result.length; i++) {
            result[i] = fun(i + 1, secretKey, coefficients, mod);
        }

        return result;
    }

    /**
     * 计算生成多项式 f(x) = secretKey + coefficients[0] * x + coefficients[1] * x ^ 2 + ... + coefficients[n] * x ^ n
     * 的值。模运算。
     *
     * @param coefficients 系数，下标 0 表示 x^1 的系数，下标 n 表示 x^(n + 1) 的系数
     * @param x            自变量
     * @param c            常数项
     * @param mod          模，所有运算为模运算
     * @return f(x)
     */
    private long fun(int x, long c, long[] coefficients, long mod) {
        LinkedList<Long> coefs = new LinkedList<>();
        for (long item : coefficients) {
            coefs.add(item);
        }

        return (fun0(x, coefs, mod) + c) % mod;
    }

    /**
     * 多项式算法（模运算）
     * f(x) = c + a1 * x^1 + a2 * x^2 + a3 * x^3 + ... + an * x^n
     * = c + x(a1 + x(a2 + x(a3 ... + x(an))))
     *
     * @param x            自变量
     * @param coefficients 系数
     * @param mod          模
     * @return f(x)
     */
    private long fun0(int x, LinkedList<Long> coefficients, long mod) {
        if (coefficients == null || coefficients.size() == 0) {
            return 0;
        }

        if (coefficients.size() == 1) {
            return (long) ((1.0 * coefficients.get(0)) * x % mod);
        }

        long a0 = coefficients.removeFirst();
        LinkedList<Long> list = new LinkedList<>(coefficients);
        return (long) ((1.0 * x * (a0 + fun0(x, list, mod))) % mod);
    }

    /**
     * 生成随机数
     *
     * @param coefficients 保存的位置
     * @param bound        最大值，不包含
     */
    private void genRandomNum(long[] coefficients, long bound) {
        Random random = new Random();
        for (int i = 0; i < coefficients.length; i++) {
            // 这里的随机数可能为负数，需要转成正数
            long r = random.nextLong();
            r = r < 0 ? r + bound : r;
            coefficients[i] = r % bound;
        }
    }

    // endregion


    @Override
    public long restoreSecretKey(long[] pieces, int totalPieceNumber, int minEffectiveSliceNumber) {
        return 0;
    }
}
