#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int k;
  in >> k;
  if(k == 1){
    out << 1;
    return;
  }
  vector<ll> fib(k);
  fib[0] = fib[1] = 1;
  for(int i = 2; i < k; i++){
    fib[i] = fib[i - 1] + fib[i - 2];
  }
  ll sum = 0;
  for(int i = 0; i < k; i++){
    sum += fib[i];
  }
  out << sum;
}

RUN_ONCE