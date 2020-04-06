#include "../../libs/common.h"

ll Comp(ll n, ll m){
  if(m == 0){
    return 1;
  }
  return Comp(n - 1, m - 1) * n / m;
}

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;

  if(k > n){
    out << 0;
    return;
  }
  ll way = 1;
  for(int i = 0; i < k; i++){
    way *= (n - i);
  }
  way *= Comp(n, k);
  out << way;
}

RUN_ONCE