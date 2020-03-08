#include "../../libs/common.h"
#include "../../libs/gcd.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  int ans = 0;
  for(int i = 1; i <= n; i++){
    if(gcd::Gcd(i, n) == 1){
      ans++;
    }
  }
  out << ans;
}

RUN_ONCE