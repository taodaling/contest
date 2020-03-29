#include "../../libs/common.h"
#include "../../libs/guass_elimination.h"
#include "../../libs/prime_sieve.h"
#include "../../libs/bigint.h"

/**
 * log_x y
 */
int Log(int x, int y){
  int ans = 0;
  while(y % x == 0){
    y /= x;
    ans++;
  }
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  int m, n;
  in >> m >> n;

  vector<int> primes;
  bitset<2000> isComp;
  prime_sieve::EulerSieve(primes, isComp);
  primes.resize(m);

  guass_elimination::ModGussianElimination<int> mge(m, n, 2);
  for (int i = 0; i < n; i++) {
    int y;
    in >> y;
    for(int j = 0; j < m; j++){
      int log = Log(primes[j], y);
      mge.mat()[j][i] = log;
    }
  }
  
  mge.solve();
  int exp = n - mge.rank();
  bigint::Bigint ans = 1;
  for(int i = 0; i < exp; i++){
    ans *= 2;
  }
  ans -= 1;
  out << ans;
}

RUN_ONCE