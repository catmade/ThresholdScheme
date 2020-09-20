package com.py7hon.entity;

import lombok.Data;

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

    public Piece() {
    }

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