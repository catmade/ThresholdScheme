package com.py7hon.threshold.scheme;

import lombok.Data;

/**
 * 定义了门限方案的一些方法
 *
 * @author Seven
 * @version 2.0
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
    Piece[] genPieces(long secretKey, int totalPieceNumber, int minEffectiveSliceNumber, long mod);

    /**
     * 根据部分分片，尝试还原出密钥
     *
     * @param pieces                  部分分片
     * @param totalPieceNumber        总分片数（n）
     * @param minEffectiveSliceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
     * @param mod                     模，所有的运算将在此模下进行。素数，大于 <code>totalPieceNumber</code> 和 <code>secretKey</code>
     * @return 还原出的密钥，如果分片不充分，则不能还原出正确的密钥
     */
    long restoreSecretKey(Piece[] pieces, int totalPieceNumber, int minEffectiveSliceNumber, long mod);

    /**
     * 块。将密钥按照某种规则分解成的密码片。对应于多项式的坐标，index表示横坐标，value表示纵坐标
     *
     * @author Seven
     * @version 1.0
     * @date 2020-09-14 22:18
     */
    @Data
    public class Piece {
        /**
         * 块下标，从 1 开始
         */
        private int index;

        /**
         * 块存储的数值
         */
        private long value;

        /**
         * 构造器
         *
         * @param index 块下标
         * @param value 块存储的数值
         */
        public Piece(int index, long value) {
            this.index = index;
            this.value = value;
        }
    }
}
