package com.py7hon.threshold.scheme.chinese.remainder.theorem;


import com.py7hon.threshold.scheme.ThresholdScheme;

/**
 * 基于中国剩余定理的(k, n)门限方案，
 *
 * @author Seven
 * @version 1.0
 * @date 2020/9/1 11:26
 */
public class ChineseRemainderTheorem implements ThresholdScheme {

    @Override
    public Piece[] genPieces(long secretKey, int totalPieceNumber, int minEffectiveSliceNumber, long mod) {
        return new Piece[0];
    }

    @Override
    public long restoreSecretKey(Piece[] pieces, int totalPieceNumber, int minEffectiveSliceNumber, long mod) {
        return 0;
    }
}
