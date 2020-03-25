#include "../../libs/common.h"

int mirror(int i, int n){
  return n - i + 1;
}

int dist(int l, int r){
  return r - l + 1;
}

int solve(int i, int n){
  if(n == 1){
    return 1;
  }
  int k = n / 2;
  if(i <= k){
    return solve(mirror(i, k), k) +  dist(k + 1, n);
  }
  return solve(mirror(i - k, dist(k + 1, n)), dist(k + 1, n));
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  int q;
  in >> n >> q;
  int ans = solve(q, n);
  out << ans;
}

RUN_ONCE