package on2019_12.on2019_12_24_.LUOGU4137;



import template.algo.MoOnArray;
import template.io.FastInput;
import template.io.FastOutput;

public class LUOGU4137 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
        }

        for (int i = 0; i < n; i++) {
            data[i] = Math.min(data[i], n);
        }

        IntHandler handler = new IntHandler(n);
        Query[] qs = new Query[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt() - 1;
            qs[i].r = in.readInt() - 1;
        }

        MoOnArray.handle(data, qs.clone(), handler);
        for (Query q : qs) {
            out.println(q.ans);
        }
    }
}

class IntHandler implements MoOnArray.IntHandler<Query> {
    int[] counts;
    int[] summaries;
    int blockSize;

    public IntHandler(int range) {
        blockSize = 1000;
        counts = new int[range + 1];
        summaries = new int[range / blockSize + 1];
    }

    @Override
    public void add(int i, int x) {
        counts[x]++;
        if (counts[x] == 1) {
            summaries[x / blockSize]++;
        }
    }

    @Override
    public void remove(int i, int x) {
        counts[x]--;
        if (counts[x] == 0) {
            summaries[x / blockSize]--;
        }
    }

    @Override
    public void answer(Query query) {
        for (int i = 0; ; i++) {
            if (summaries[i] == blockSize) {
                continue;
            }
            for (int j = i * blockSize; ; j++) {
                if (counts[j] == 0) {
                    query.ans = j;
                    return;
                }
            }
        }
    }
}

class Query implements MoOnArray.Query {
    int l;
    int r;
    int ans;

    @Override
    public int getL() {
        return l;
    }

    @Override
    public int getR() {
        return r;
    }
}
