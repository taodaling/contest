package on2021_09.on2021_09_26_AtCoder___AtCoder_Regular_Contest_127.D___Sum_of_Min_of_Xor;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class DSumOfMinOfXor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int H = 20;
        int n = in.ri();

        Node root = new Node(H + 1);
        int[] A = in.ri(n);
        int[] B = in.ri(n);
        long ans = 0;
        for(int i = 0; i < n; i++){
            ans += search(root, A[i], B[i], H);
            add(root, A[i], B[i], H);
        }
        out.println(ans);
    }

    public long search(Node root, int a, int b, int h) {
        if (root == null) {
            return 0;
        }
        long ans0 = search(root.next[Bits.get(a ^ b, h)], a, b, h - 1);
        long ans1 = deltaSearch(root.next[Bits.get(a ^ b, h) ^ 1], a, b, h - 1);
        //choice
        int abit = Bits.get(a, h + 1);
        int size = root.size[abit ^ 1];
        long current = (1L << h + 1) * size;

        return ans0 + ans1 + current;
    }

    public long deltaSearch(Node root, int a, int b, int h) {
        if (root == null) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < 2; i++) {
            int av = Bits.get(a, h + 1) ^ i;
            int bv = 1 ^ av;

            int choice = av < bv ? 0 : 1;
            int v = choice == 0 ? a : b;
            int total = root.size[i];
            for (int j = 0; j <= h; j++) {
                int one = root.bitSum[i][choice][j];
                int zero = total - one;
                assert zero >= 0;
                if (Bits.get(v, j) == 1) {
                    sum += (1L << j) * zero;
                } else {
                    sum += (1L << j) * one;
                }
            }
        }
        return sum;
    }

    public void add(Node root, int a, int b, int h) {
        root.size[Bits.get(a, h + 1)]++;
        if (h < 0) {
            return;
        }
        add(root.getOrCreate(h, Bits.get(a ^ b, h)), a, b, h - 1);
        for (int i = 0; i <= h; i++) {
            root.bitSum[Bits.get(a, h + 1)][0][i] += Bits.get(a, i);
            root.bitSum[Bits.get(a, h + 1)][1][i] += Bits.get(b, i);
        }
    }
}

class Node {
    Node[] next = new Node[2];
    int[][][] bitSum;
    int[] size;

    public int size(int i, int j) {
        if (next[i] == null) {
            return 0;
        }
        return next[i].size[j];
    }

    public Node(int h) {
        bitSum = new int[2][2][h];
        size = new int[2];
    }

    Node getOrCreate(int height, int i) {
        if (next[i] == null) {
            next[i] = new Node(height);
        }
        return next[i];
    }
}