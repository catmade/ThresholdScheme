package com.py7hon.threshold.scheme.shamir;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

/**
 * 求模工具
 * <p>
 * 参考： <a href="https://blog.csdn.net/weixin_41596280/article/details/82562609">通过列表法求模逆+Java代码</a>
 *
 * @author Seven
 * @version 1.1
 * @date 2020-09-14 22:54
 */
public class ModUtil {

    /**
     * num*b=1 mod mod 结果返回b 即 a的逆元
     *
     * @param num 数
     * @param mod 模
     * @return 模逆
     */
    public static long modInverse(long num, long mod) {
        // TODO 需要重写     [ Seven 2020/09/14 23:41 ]
        BigInteger inverse = BigInteger.valueOf(num).modInverse(BigInteger.valueOf(mod));
        return inverse.longValue();
    }

    @Test
    public void modInverse() {
        Assertions.assertEquals(13, ModUtil.modInverse(3, 19));
    }
}