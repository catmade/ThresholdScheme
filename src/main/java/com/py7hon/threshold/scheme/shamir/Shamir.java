package com.py7hon.threshold.scheme.shamir;

import com.py7hon.threshold.scheme.ThresholdScheme;

import java.math.BigInteger;

/**
 * Shamir(k, n) 门限方案
 *
 * @author Seven
 * @version 1.0
 * @date 2020/9/1 11:19
 */
public class Shamir implements ThresholdScheme {

    @Override
    public BigInteger[] genSlices(BigInteger secretKey, int totalSliceNumber, int minEffectiveSliceNumber) {
        return new BigInteger[0];
    }

    @Override
    public BigInteger restoreSecretKey(BigInteger[] slices, int totalSliceNumber, int minEffectiveSliceNumber) {
        return null;
    }
}
