#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  int ans = n / 3 * 2;
  if(n % 3 == 2){
    ans++;
  }
  out << ans << endl;
}

RUN_ONCE