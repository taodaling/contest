package on2020_07.on2020_07_01_Codeforces___Codeforces_Round__402__Div__1_.B__Bitwise_Formula;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashMap;
import java.util.Map;

public class BBitwiseFormula {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Constant unknown = new Constant(0);
        Operand[] ops = new Operand[n];
        Map<String, Operand> map = new HashMap<>(n + 1);
        map.put("?", unknown);

        for (int i = 0; i < n; i++) {
            in.skipBlank();
            String line = in.readLine();
            String[] parts = line.split(" ");
            String name = parts[0];
            Operand op = null;
            if (parts.length == 3) {
                //constant
                op = new BitOneArray(parts[2]);
            } else {
                Operand left = map.get(parts[2]);
                Operand right = map.get(parts[4]);
                switch (parts[3]) {
                    case "AND":
                        op = new AndOp(left, right);
                        break;
                    case "OR":
                        op = new OrOp(left, right);
                        break;
                    case "XOR":
                        op = new XorOp(left, right);
                        break;
                }
            }

            ops[i] = op;
            map.put(name, op);
        }

        BitSet min = new BitSet(m);
        BitSet max = new BitSet(m);
        for (int i = 0; i < m; i++) {
            unknown.modify(0);
            int v0 = collect(ops, i);
            unknown.modify(1);
            int v1 = collect(ops, i);

            if (v0 <= v1) {
                min.set(i, false);
            } else {
                min.set(i, true);
            }

            if (v0 >= v1) {
                max.set(i, false);
            } else {
                max.set(i, true);
            }
        }

        for (int i = 0; i < m; i++) {
            out.append(min.get(i) ? 1 : 0);
        }
        out.println();
        for (int i = 0; i < m; i++) {
            out.append(max.get(i) ? 1 : 0);
        }
    }

    public int collect(Operand[] ops, int v) {
        int ans = 0;
        for (Operand op : ops) {
            op.reset();
            ans += op.value(v);
        }
        return ans;
    }
}

interface Operand {
    int value(int v);

    void reset();
}

class Constant implements Operand {
    int x;

    public void modify(int x) {
        this.x = x;
    }

    public Constant(int x) {
        this.x = x;
    }

    @Override
    public int value(int v) {
        return x;
    }

    @Override
    public void reset() {

    }
}

class BitOneArray implements Operand {
    String s;

    public BitOneArray(String s) {
        this.s = s;
    }

    @Override
    public int value(int v) {
        return s.charAt(v) - '0';
    }

    @Override
    public void reset() {

    }
}

abstract class AbstractCachedOp implements Operand {
    private int val;
    private boolean visited;

    @Override
    public int value(int v) {
        if (!visited) {
            visited = true;
            val = compute(v);
        }
        return val;
    }

    public void reset() {
        visited = false;
    }

    public abstract int compute(int v);
}

class XorOp extends AbstractCachedOp {
    public XorOp(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    private Operand left;
    private Operand right;

    @Override
    public int compute(int v) {
        return left.value(v) ^ right.value(v);
    }
}

class AndOp extends AbstractCachedOp {
    private Operand left;
    private Operand right;

    public AndOp(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int compute(int v) {
        return left.value(v) & right.value(v);
    }
}

class OrOp extends AbstractCachedOp {
    private Operand left;
    private Operand right;

    public OrOp(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int compute(int v) {
        return left.value(v) | right.value(v);
    }
}