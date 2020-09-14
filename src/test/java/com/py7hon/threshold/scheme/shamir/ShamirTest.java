package com.py7hon.threshold.scheme.shamir;

import com.py7hon.threshold.scheme.ThresholdScheme;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ShamirTest {

    @Test
    public void genSlices() {
        ThresholdScheme.Piece[] pieces = new Shamir().genPieces(11, 5, 3, 19);
        System.out.println("pieces = " + Arrays.toString(pieces));
    }
}