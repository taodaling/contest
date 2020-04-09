#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<pair<int, int>> props(n);
  for (int i = 0; i < n; i++) {
    in >> X(props[i]);
  }
  for(int i = 0; i < n; i++){
    in >> Y(props[i]);
  }

  int ans = 0;
  sort(props.begin(), props.end(),
       [&](auto &a, auto &b) { return Y(a) > Y(b); });

  int ps = 0;
  for (auto &p : props) {
    ps += X(p);
    ans = max(ans, ps + Y(p));
  }

  out << ans ;
}

RUN_ONCE