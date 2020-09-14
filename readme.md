# 密码学门限方案实现

分出来的块

```java
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
```



## 一、Shamir(k, n)门限方案

>   参考：
>
>   [1] Shamir 门限方案介绍：[Secret Sharing Explained Visually](https://www.youtube.com/watch?v=iFY5SyY3IMQ)

### 1. 密码块生成算法

#### 1）多项式求值算法

```java
/**
 * 计算生成多项式 f(x) = secretKey + coefficients[0] * x + coefficients[1] * x ^ 2 + ... + coefficients[n] * x ^ n
 * 的值。模运算。
 *
 * @param coefficients 系数，下标 0 表示 x^1 的系数，下标 n 表示 x^(n + 1) 的系数
 * @param x            自变量
 * @param c            常数项
 * @param mod          模，所有运算为模运算
 * @return f(x)
 */
private long fun(int x, long c, long[] coefficients, long mod) {
    LinkedList<Long> coefs = new LinkedList<>();
    for (long item : coefficients) {
        coefs.add(item);
    }

    return (fun0(x, coefs, mod) + c) % mod;
}

/**
 * 多项式算法（模运算）
 * f(x) = c + a1 * x^1 + a2 * x^2 + a3 * x^3 + ... + an * x^n
 * = c + x(a1 + x(a2 + x(a3 ... + x(an))))
 *
 * @param x            自变量
 * @param coefficients 系数
 * @param mod          模
 * @return f(x)
 */
private long fun0(int x, LinkedList<Long> coefficients, long mod) {
    if (coefficients == null || coefficients.size() == 0) {
        return 0;
    }

    if (coefficients.size() == 1) {
        return (long) ((1.0 * coefficients.get(0)) * x % mod);
    }

    long a0 = coefficients.removeFirst();
    LinkedList<Long> list = new LinkedList<>(coefficients);
    return (long) ((1.0 * x * (a0 + fun0(x, list, mod))) % mod);
}
```

#### 2）随机系数生成算法

```java
/**
 * 生成随机数
 *
 * @param coefficients 保存的位置
 * @param bound        最大值，不包含
 */
private void genRandomNum(long[] coefficients, long bound) {
    Random random = new Random();
    for (int i = 0; i < coefficients.length; i++) {
        // 这里的随机数可能为负数，需要转成正数
        coefficients[i] = Math.floorMod(random.nextLong(), bound);
    }
}
```

#### 3）密钥块生成算法

```java
/**
 * 计算分片
 *
 * @param secretKey               密钥
 * @param totalPieceNumber        总分片数（n）
 * @param minEffectiveSliceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
 * @param mod                     模，所有的运算将在此模下进行。素数，大于 <code>totalPieceNumber</code> 和 <code>secretKey</code>
 * @return 分片后的数据
 */
@Override
public Piece[] genPieces(long secretKey, int totalPieceNumber, int minEffectiveSliceNumber, long mod) {
    // 1. 随机生成多项式的系数。多项式的项数为：totalPieceNumber - minEffectiveSliceNumber
    // 系数，下标 0 表示 x^1 的系数，下标 n 表示 x^(n + 1) 的系数
    long[] coefficients = new long[totalPieceNumber - minEffectiveSliceNumber];
    // 生成随机数，取值范围为 [0, mod)
    genRandomNum(coefficients, mod);

    // 生成多项式为 f(x) = secretKey + coefficients[0] * x + coefficients[1] * x ^ 2 + ... + coefficients[n] * x ^ n
    // 2. 求每个块，即求 f(1), f(2) ... f(totalPieceNumber)
    Piece[] result = new Piece[totalPieceNumber];
    for (int i = 0; i < result.length; i++) {
        long value = fun(i + 1, secretKey, coefficients, mod);
        result[i] = new Piece(i + 1, value);
    }

    return result;
}
```

### 2. 密钥恢复算法

#### 1）欧几里得插值多项式求解

```java
/**
 * 根据部分分片，尝试还原出密钥
 *
 * @param pieces                  部分分片
 * @param totalPieceNumber        总分片数（n）
 * @param minEffectiveSliceNumber 最小有效分片数（k），取 k 个以上的分片才能还原出密钥
 * @param mod                     模，所有的运算将在此模下进行。素数，大于 <code>totalPieceNumber</code> 和 <code>secretKey</code>
 * @return 还原出的密钥，如果分片不充分，则不能还原出正确的密钥
 */
@Override
public long restoreSecretKey(Piece[] pieces, long mod) {
    long x = 0;
    // 这里使用 double 是为了防止 long 数值溢出
    double result = 0;
    for (Piece piece : pieces) {
        // 这里使用 double 是为了防止 long 数值溢出
        double a = piece.getValue();
        for (Piece p : pieces) {
            if (p != piece) {
                long inverse = ModUtil.modInverse(piece.getIndex() - p.getIndex(), mod);
                a *= (x - p.getIndex()) * inverse;
                a = Math.floorMod((long) (a % mod), mod);
            }
        }
        result += a;
        result = Math.floorMod((int) (result % mod), mod);
    }
    return (long) result;
}
```

## 二、基于中国剩余定理的(k, n)门限方案