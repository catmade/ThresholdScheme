package com.py7hon.controller;

import com.py7hon.entity.Piece;
import com.py7hon.threshold.scheme.ThresholdScheme;
import com.py7hon.threshold.scheme.chinese.remainder.theorem.ChineseRemainderTheorem;
import com.py7hon.threshold.scheme.shamir.Shamir;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门限方案接口
 *
 * @author Seven
 * @version 1.0
 * @date 2020/9/19 23:10
 */
@RestController
public class ThresholdSchemeController {

    @GetMapping("/api/generate-piece/{type}")
    public ResponseEntity<?> genPiece(
            @PathVariable("type") String type,
            @RequestParam("secretKey") long secretKey,
            @RequestParam("totalPieceNumber") int totalPieceNumber,
            @RequestParam("minEffectivePieceNumber") int minEffectivePieceNumber,
            @RequestParam("mod") long mod
    ) {
        ThresholdScheme thresholdScheme = getThresholdScheme(type);

        if (thresholdScheme == null) {
            return ResponseEntity.badRequest().build();
        }

        Piece[] pieces = thresholdScheme.genPieces(secretKey, totalPieceNumber, minEffectivePieceNumber, mod);
        return ResponseEntity.ok(pieces);
    }

    @RequestMapping("/api/restore-secret-key/{type}")
    public ResponseEntity<?> restoreSecretKey(
            @PathVariable("type") String type,
            @RequestParam("mod") long mod,
            @RequestBody List<Piece> pieces
    ) {
        ThresholdScheme thresholdScheme = getThresholdScheme(type);

        if (thresholdScheme == null) {
            return ResponseEntity.badRequest().build();
        }

        long secretKey = thresholdScheme.restoreSecretKey(pieces.toArray(new Piece[0]), 1, 1, mod);
        return ResponseEntity.ok(secretKey);
    }

    /**
     * 根据类别获取门限方案
     *
     * @param type 类别。shamir 或 chinese-remainder
     * @return 门限方案
     */
    private ThresholdScheme getThresholdScheme(String type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case "shamir":
                return new Shamir();
            case "chinese-remainder":
                return new ChineseRemainderTheorem();
            default:
                return null;
        }
    }
}
