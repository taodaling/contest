#include "../../libs/common.h"
#include "../../libs/presum.h"

void solve(int testId, istream &in, ostream &out) {
  string a;
  string b;
  in >> a >> b;
  if(a.length() != b.length()){
    out << -1;
    return;
  }
  int n = a.length();
  vector<int> va(n);
  vector<int> vb(n);
  for(int i = 0; i < n; i++){
    va[i] = a[i] == '+' ? 1 : 0;
    vb[i] = b[i] == '+' ? 1 : 0;
  }
  pre_sum::PreSum<int> pa(va);
  pre_sum::PreSum<int> pb(vb);
  int ans = 0;

  if(pa.prefix(n - 1) != pb.prefix(n - 1)){
    out << -1;
    return;
  }

  for(int i = 0; i < n; i++){
    ans += abs(pa.prefix(i) - pb.prefix(i));
  }

  out << ans;
}

RUN_ONCE