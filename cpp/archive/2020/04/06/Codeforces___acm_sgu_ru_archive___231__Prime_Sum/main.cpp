#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/prime_sieve.h"

void solve(int testId, istream &in, ostream &out) {
  bitset<1000010> isComp;
  vector<int> primes;
  primes.reserve(1000010);
  prime_sieve::EulerSieve(primes, isComp);
  int n;
  in >> n;
  vector<pair<int, int>> ans;
  ans.reserve(1000010);
  for (int i = 2; i + 2 <= n; i++) {
    if (!isComp[i] && !isComp[i + 2]) {
      ans.emplace_back(2, i);
    }
  }

  out << ans.size() << endl;

  for(auto &p : ans){
    out << X(p) << ' ' << Y(p) << endl;
  }
}

RUN_ONCE