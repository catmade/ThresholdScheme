package com.py7hon.threshold.scheme;

import java.math.BigInteger;

/**
 * 定义了门限方案的一些方法
 *
 * @author Seven
 * @version 1.0
 * @date 2020/9/1 11:44
 */
public interface ThresholdScheme {
    /**
     * 计算分片
     *
     * @param secretKey               密钥
     * @param totalSliceNumber        总分片数（n）
     * @param minEffectiveSliceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
     * @return 分片后的数据
     */
    BigInteger[] genSlices(BigInteger secretKey, int totalSliceNumber, int minEffectiveSliceNumber);

    /**
     * 根据部分分片，尝试还原出密钥
     *
     * @param slices                  部分分片
     * @param totalSliceNumber
     * @param minEffectiveSliceNumber
     * @return 还原出的密钥，如果分片不充分，则不能还原出正确的密钥
     */
    BigInteger restoreSecretKey(BigInteger[] slices, int totalSliceNumber, int minEffectiveSliceNumber);
}
