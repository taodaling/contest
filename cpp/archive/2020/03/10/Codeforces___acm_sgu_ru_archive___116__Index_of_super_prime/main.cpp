#include "../../libs/common.h"
#include "../../libs/prime_sieve.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  vector<int> prime;
  prime.reserve(10000);
  bitset<10001> bs;
  prime_sieve::EulerSieve(prime, bs);
  vector<int> sp;
  for (int i = 0; i < prime.size() && prime[i] - 1 < prime.size(); i++) {
    sp.push_back(prime[prime[i] - 1]);
  }
  dbg(sp);
  int n;
  in >> n;
  vector<int> dp(n + 1);
  vector<int> prev(n + 1);
  fill(dp.begin(), dp.end(), 1e8);
  fill(prev.begin(), prev.end(), -1);
  dp[0] = 0;
  for (int i = 1; i <= n; i++) {
    for (int x : sp) {
      if (x > i) {
        break;
      }
      if (dp[i - x] + 1 < dp[i]) {
        dp[i] = dp[i - x] + 1;
        prev[i] = i - x;
      }
    }
  }

  if (n == 0) {
    out << 0 << endl;
    return;
  }

  vector<int> ans;
  if (prev[n] != -1) {
    for (int i = n; prev[i] != -1; i = prev[i]) {
      ans.push_back(i - prev[i]);
    }
  }

  sort(ans.begin(), ans.end(), std::greater<int>());
  out << ans.size() << endl;
  for (int x : ans) {
    out << x << ' ';
  }
}

RUN_ONCE