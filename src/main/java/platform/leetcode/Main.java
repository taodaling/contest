package platform.leetcode;



import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        //new Solution().countInterestingSubarrays(Arrays.asList(3,2,4), 2, 1);
        new Solution().countCompleteSubstrings("az", 1);
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static
    class Solution {
        private long mod = 2305843009213693951L;
        Random random = new Random();

        public long mul(long a, long b, long mod) {
            if(b == 0) {
                return a;
            }
            long ans = mul(a, b / 2, mod) << 1;
            if(b % 2 == 1) {
                ans += a;
            }
            return ans % mod;
        }
        public int countCompleteSubstrings(String word, int k) {
            long[] eval = new long[26];
            long[] vals = new long[26];
            for(int i = 0; i < 26; i++) {
                eval[i] = random.nextLong(mod);
                vals[i] = mul(eval[i], k, mod);
            }
            int ans = 0;
            Map<Long, Integer> map = new HashMap<>();
            map.put(0L, 1);
            int n = word.length();
            List<Character> latest = new ArrayList<>();
            long ps = 0;
            for(int i = 0; i < n; i++) {
                if(i > 0 && Math.abs((int)word.charAt(i) - (int)word.charAt(i - 1)) > 2) {
                    map.clear();
                    map.put(0L, 1);
                    ps = 0;
                }
                char c = word.charAt(i);
                ps += eval[c - 'a'];
                latest.remove((Character)c);
                latest.add(c);

                long suf = 0;
                for(int j = latest.size(); j >= 0; j--) {
                    suf += vals[latest.get(j) - 'a'];
                    suf %= mod;

                    long search = ps - suf;
                    if(search < 0) {
                        search += mod;
                    }
                    ans += map.getOrDefault(search, 0);
                }

                map.put(ps, map.getOrDefault(ps, 0) + 1);
            }

            return ans;
        }
    }

}