package com.py7hon.threshold.scheme;

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
     * @param totalPieceNumber        总分片数（n）
     * @param minEffectiveSliceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
     * @param mod                     模，所有的运算将在此模下进行。素数，大于 <code>totalPieceNumber</code> 和 <code>secretKey</code>
     * @return 分片后的数据
     */
    long[] genPieces(long secretKey, int totalPieceNumber, int minEffectiveSliceNumber, long mod);

    /**
     * 根据部分分片，尝试还原出密钥
     *
     * @param pieces                  部分分片
     * @param totalPieceNumber        总分片数（n）
     * @param minEffectiveSliceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
     * @return 还原出的密钥，如果分片不充分，则不能还原出正确的密钥
     */
    long restoreSecretKey(long[] pieces, int totalPieceNumber, int minEffectiveSliceNumber);
}
