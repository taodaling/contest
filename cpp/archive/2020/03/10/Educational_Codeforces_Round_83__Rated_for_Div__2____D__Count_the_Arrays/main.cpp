#include "../../libs/common.h"
#include "../../libs/modular.h"

using Mint = modular::Modular<int, 998244353>;

Mint fact(int n){
  if(n == 0){
    return Mint(1);
  }
  return Mint(n) * fact(n - 1);
}

Mint comp(int n, int m){
  if(n < m){
    return 0;
  }
  return fact(n) / fact(m) / fact(n - m);
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  Mint ans = comp(m, n - 1) * (n - 2);
  if(n >= 3) {
    ans *= Mint(2).pow(n - 3);
  }
  out << ans << endl;
}

RUN_ONCE