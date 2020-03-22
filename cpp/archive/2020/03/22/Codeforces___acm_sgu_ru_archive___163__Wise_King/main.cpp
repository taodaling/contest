#include "../../libs/common.h"

int Exp(int x, int n){
  return n == 0 ? 1 : (Exp(x, n - 1) * x);
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  int ans = 0;
  for(int i = 0; i < n; i++){
    int x;
    in >> x;
    ans += max(0, Exp(x, m));
  }
  out << ans;
}

RUN_ONCE