package contest;

import template.algo.BlockChain;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.Buffer;
import template.utils.Debug;
import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.concurrent.BlockingDeque;

public class DSeregaAndFun {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        BlockChain<Sum, Void, Integer, BlockImpl> bc = new BlockChain<>(n, BlockImpl.B, (l, r) -> {
            return BlockImpl.newInstance(a, l, r);
        });
        int q = in.ri();
        int lastans = 0;
        for (int i = 0; i < q; i++) {
            debug.debug("bc", bc);
            int t = in.ri();
            int l = in.ri();
            int r = in.ri();
            if (!debug.enable()) {
                l = (l + lastans - 1) % n;
                r = (r + lastans - 1) % n;
                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
            }else{
                l--;
                r--;
            }
            if (t == 1) {
                Integer last = bc.get(r);
                bc.delete(r);
                bc.insert(l, last);
            } else {
                int k = in.ri();
                if (!debug.enable()) {
                    k = (k + lastans - 1) % n + 1;
                }
                Sum sum = new Sum();
                sum.x = k;
                bc.query(l, r, sum);
                lastans = sum.cnt;
                out.println(lastans);
            }
        }
    }
}

class Sum {
    int cnt;
    int x;
}

class BlockImpl implements BlockChain.Block<Sum, Void, Integer, BlockImpl> {
    static int B = 300;
    static Buffer<BlockImpl> alloc = new Buffer<>(() -> new BlockImpl(), entity -> {
        entity.size = 0;
    });
    IntegerHashMap map = new IntegerHashMap(B * 2, false);
    int[] data = new int[B * 2];
    int size = 0;

    @Override
    public void insert(int index, Integer integer) {
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }
        data[index] = integer;
        size++;
        map.modify(integer, 1);
    }

    @Override
    public void delete(int index) {
        map.modify(data[index], -1);
        for(int i = index + 1; i < size; i++){
            data[i - 1] = data[i];
        }
        size--;
    }

    public static BlockImpl newInstance(int[] data, int l, int r) {
        BlockImpl ans = alloc.alloc();
        ans.init(data, l, r);
        return ans;
    }

    public void init(int[] data, int l, int r) {
        size = r - l + 1;
        for (int i = l; i <= r; i++) {
            this.data[i - l] = data[i];
        }
        afterPartialUpdate();
    }

    @Override
    public Pair<BlockImpl, BlockImpl> split(int n) {
        BlockImpl pre = newInstance(data, 0, n - 1);
        BlockImpl post = newInstance(data, n, size - 1);
        alloc.release(this);
        return new Pair<>(pre, post);
    }

    @Override
    public BlockImpl merge(BlockImpl block) {
        beforePartialQuery();
        block.beforePartialQuery();
        for (int i = 0; i < block.size; i++) {
            data[size++] = block.data[i];
        }
        afterPartialUpdate();
        alloc.release(block);
        return this;
    }

    @Override
    public void fullyQuery(Sum sum) {
        sum.cnt += map.get(sum.x);
    }

    @Override
    public void partialQuery(int index, Sum sum) {
        if (data[index] == sum.x) {
            sum.cnt++;
        }
    }

    @Override
    public void fullyUpdate(Void upd) {

    }

    @Override
    public void partialUpdate(int index, Void upd) {

    }

    @Override
    public void afterPartialUpdate() {
        map.clear();
        for (int i = 0; i < size; i++) {
            map.modify(data[i], 1);
        }
    }

    @Override
    public String toString() {
        int[] d = Arrays.copyOf(data, size);
        return Arrays.toString(d);
    }

    @Override
    public void beforePartialQuery() {
    }

    @Override
    public Integer get(int index) {
        return data[index];
    }
}