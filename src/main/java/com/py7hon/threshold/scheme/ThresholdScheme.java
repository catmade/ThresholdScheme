package com.py7hon.threshold.scheme;

import com.py7hon.entity.Piece;

/**
 * 定义了门限方案的一些方法
 *
 * @author Seven
 * @version 2.0
 * @date 2020/9/1 11:44
 */
public interface ThresholdScheme {
    /**
     * 计算分片。
     * <p>调用说明：</p>
     * <P>1. 确定好要进行分片的密钥：secretKey（[0, Long.MAX_VALUE]）</P>
     * <p>2. 确定好分片数：totalPieceNumber ([0, Integer.MAX_VALUE])</p>
     * <p>3. 确定最小有效分片数：minEffectivePieceNumber ([0, totalPieceNumber])</p>
     * <p>4. 任取一个大于的素数：mod (mod >  <code>totalPieceNumber</code> 且 mod > <code>secretKey</code> )，用作模运算的模</p>
     * <p>5. 调用方法即可得到分片。任选 n (n >= minEffectivePieceNumber)个分片即可还原出密钥</p><br>
     *
     * @param secretKey               密钥
     * @param totalPieceNumber        总分片数（n）
     * @param minEffectivePieceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
     * @param mod                     模，所有的运算将在此模下进行。素数，大于 <code>totalPieceNumber</code> 和 <code>secretKey</code>
     * @return 分片后的数据
     */
    Piece[] genPieces(long secretKey, int totalPieceNumber, int minEffectivePieceNumber, long mod);

    /**
     * 根据部分分片，尝试还原出密钥
     *
     * @param pieces                  部分分片
     * @param totalPieceNumber        总分片数（n）
     * @param minEffectivePieceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
     * @param mod                     模，所有的运算将在此模下进行。素数，大于 <code>totalPieceNumber</code> 和 <code>secretKey</code>
     * @return 还原出的密钥，如果分片不充分，则不能还原出正确的密钥
     */
    long restoreSecretKey(Piece[] pieces, int totalPieceNumber, int minEffectivePieceNumber, long mod);

}
