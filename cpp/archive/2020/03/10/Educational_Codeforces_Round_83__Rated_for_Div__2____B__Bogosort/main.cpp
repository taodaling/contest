#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> a(n);
  for(int i = 0; i < n; i++){
    in >> a[i];
  }
  sort(a.begin(), a.end());
  reverse(a.begin(), a.end());
  for(int x : a){
    out << x << ' ';
  }
  out << endl;
}

RUN_MULTI