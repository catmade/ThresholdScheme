package com.py7hon.threshold.scheme.shamir;

import com.py7hon.threshold.scheme.ThresholdScheme;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    public Piece[] genPieces(long secretKey, int totalPieceNumber, int minEffectiveSliceNumber, long mod) {
        // 1. 随机生成多项式的系数。多项式的项数为：totalPieceNumber - minEffectiveSliceNumber
        // 系数，下标 0 表示 x^1 的系数，下标 n 表示 x^(n + 1) 的系数
        long[] coefficients = new long[totalPieceNumber - minEffectiveSliceNumber];
        // 生成随机数，取值范围为 [0, mod)
        genRandomNum(coefficients, mod);

        // 生成多项式为 f(x) = secretKey + coefficients[0] * x + coefficients[1] * x ^ 2 + ... + coefficients[n] * x ^ n
        // 2. 求每个块，即求 f(1), f(2) ... f(totalPieceNumber)
        Piece[] result = new Piece[totalPieceNumber];
        for (int i = 0; i < result.length; i++) {
            long value = fun(i + 1, secretKey, coefficients, mod);
            result[i] = new Piece(i + 1, value);
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

    @Test
    void testFun() {
        long[] coefficients = {2, 7};
        assertEquals(1, fun(1, 11, coefficients, 19));
        assertEquals(5, fun(2, 11, coefficients, 19));
        assertEquals(4, fun(3, 11, coefficients, 19));
        assertEquals(17, fun(4, 11, coefficients, 19));
        assertEquals(6, fun(5, 11, coefficients, 19));
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
            coefficients[i] = Math.floorMod(random.nextLong(), bound);
        }
    }

    // endregion

    // region ============= 通过密码片解析出密码 =============

    @Override
    public long restoreSecretKey(Piece[] pieces, int totalPieceNumber, int minEffectiveSliceNumber, long mod) {
        // 使用欧几里得插值多项式求解 x = 0 时多项式的值
        long x = 0;
        // 这里使用 double 是为了防止 long 数值溢出
        double result = 0;
        for (Piece piece : pieces) {
            // 这里使用 double 是为了防止 long 数值溢出
            double a = piece.getValue();
            for (Piece p : pieces) {
                if (p != piece) {
                    long inverse = ModUtil.modInverse(piece.getIndex() - p.getIndex(), mod);
                    a *= (x - p.getIndex()) * inverse;
                    a = Math.floorMod((long) (a % mod), mod);
                }
            }
            result += a;
            result = Math.floorMod((int) (result % mod), mod);
        }
        return (long) result;
    }

    // endregion

    @Test
    public void restoreSecretKeyTest() {
        Piece[] pieces = {
                new Piece(1, 1),
                new Piece(2, 5),
                new Piece(3, 4),
                new Piece(4, 17),
                new Piece(5, 6)
        };

        Piece[] p1 = {pieces[1], pieces[2], pieces[4]};
        Piece[] p2 = {pieces[1], pieces[2], pieces[3]};
        Piece[] p3 = {pieces[1], pieces[3], pieces[4]};
        Piece[] p4 = {pieces[1], pieces[2], pieces[3], pieces[4]};
        Piece[] p5 = {pieces[3], pieces[4]};
        long secretKey = 11;
        long mod = 19;
        assertEquals(secretKey, restoreSecretKey(p1, 0, 0, mod));
        assertEquals(secretKey, restoreSecretKey(p2, 0, 0, mod));
        assertEquals(secretKey, restoreSecretKey(p3, 0, 0, mod));
        assertEquals(secretKey, restoreSecretKey(p4, 0, 0, mod));
        assertNotEquals(secretKey, restoreSecretKey(p5, 0, 0, mod));
    }
}
