#include "../../libs/common.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  int n, m, k;
  in >> n >> m >> k;
  vector<int> prime;
  vector<int> cnts;

  for (int i = 2; i * i <= k; i++) {
    if (k % i != 0) {
      continue;
    }
    prime.push_back(i);
    cnts.push_back(0);
    while (k % i == 0) {
      k /= i;
      cnts.back()++;
    }
  }
  if (k > 1) {
    prime.push_back(k);
    cnts.push_back(1);
  }

  dbg(prime, cnts);

  int ans = 0;
  for (int i = 0; i < n; i++) {
    int x;
    in >> x;
    bool valid = true;
    for (int i = 0; i < prime.size(); i++) {
      int p = prime[i];
      int time = 0;
      while (x % p == 0) {
        time++;
        x /= p;
      }
      if (time * m < cnts[i]) {
        valid = false;
      }
    }

    if (valid) {
      ans++;
    }
  }

  out << ans << endl;
}

RUN_ONCE