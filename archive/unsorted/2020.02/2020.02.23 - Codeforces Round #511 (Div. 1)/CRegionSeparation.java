package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.LongPollardRho;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongObjectHashMap;

import java.util.ArrayList;
import java.util.List;

public class CRegionSeparation {
    long[] primes;
    int[] cnts;
    LongObjectHashMap<Factor> map;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].w = in.readInt();
        }
        for (int i = 2; i <= n; i++) {
            Node p = nodes[in.readInt()];
            p.next.add(nodes[i]);
        }
        dfsForSum(nodes[1]);
        long sum = nodes[1].sum;
        primes = new LongPollardRho().findAllFactors(sum)
                .keySet().stream().mapToLong(Long::valueOf)
                .toArray();
        cnts = new int[primes.length];
        for (int i = 0; i < primes.length; i++) {
            int time = 0;
            long x = sum;
            while (x % primes[i] == 0) {
                x /= primes[i];
                time++;
            }
            cnts[i] = time;
        }

        List<Factor> factorList = new ArrayList<>(30000);
        collect(new int[primes.length], 0, 1, factorList);
        Factor[] allFactors = factorList.toArray(new Factor[0]);
        map = new LongObjectHashMap<>(allFactors.length, false);
        for (Factor factor : allFactors) {
            map.put(factor.val, factor);
        }

        for (int i = 1; i <= n; i++) {
            long g = GCDs.gcd(nodes[i].sum, sum);
            if (!map.containKey(g)) {
                throw new RuntimeException();
            }
            map.get(g).appearance++;
        }

        for (int i = 0; i < allFactors.length; i++) {
            for (int j = i + 1; j < allFactors.length; j++) {
                if (allFactors[j].val % allFactors[i].val == 0) {
                    allFactors[i].appearance += allFactors[j].appearance;
                }
            }
        }

        Modular mod = new Modular(1e9 + 7);
        for (int i = 0; i < allFactors.length; i++) {
            if (allFactors[i].appearance * allFactors[i].val != sum) {
                continue;
            }
            allFactors[i].way = 1;
            for (int j = i - 1; j >= 0; j--) {
                if (allFactors[i].val % allFactors[j].val == 0) {
                    allFactors[i].way = mod.plus(allFactors[i].way, allFactors[j].way);
                }
            }
        }

        long ans = map.get(sum).way;
        out.println(ans);
    }


    public void collect(int[] nums, int i, long val, List<Factor> factors) {
        if (i == primes.length) {
            factors.add(new Factor(nums.clone(), val));
            return;
        }
        for (int j = 0; j <= cnts[i]; j++, val *= primes[i]) {
            nums[i] = j;
            collect(nums, i + 1, val, factors);
        }
    }

    public static void dfsForSum(Node root) {
        root.sum = root.w;
        for (Node node : root.next) {
            dfsForSum(node);
            root.sum += node.sum;
        }
    }
}

class Factor {
    int[] cnts;
    long val;
    long appearance;
    int way;

    public Factor(int[] cnts, long val) {
        this.cnts = cnts;
        this.val = val;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    long sum;
    int w;
}