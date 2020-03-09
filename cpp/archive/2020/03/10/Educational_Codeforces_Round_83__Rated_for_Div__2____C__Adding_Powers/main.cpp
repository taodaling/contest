#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  vector<int> cnt(64);
  for(int i = 0; i < n; i++){
    ll a;
    in >> a;
    for(int j = 0; j < 64; j++){
      cnt[j] += a % k;
      a /= k;
    }
  }

  for(int i = 0; i < 64; i++){
    if(cnt[i] > 1){
      out << "NO" << endl;
      return;
    }
  }

  out << "YES" << endl;
}

RUN_MULTI