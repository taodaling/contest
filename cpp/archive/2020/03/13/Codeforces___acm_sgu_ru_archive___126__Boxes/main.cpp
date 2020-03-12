#include "../../libs/bits.h"
#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  ll a, b;
  in >> a >> b;
  if (a > b) {
    swap(a, b);
  }

  if (a == 0) {
    out << 0;
    return;
  }

  ll n = a + b;
  ll k = 1;
  while (a % 2 == 0 && n % 2 == 0) {
    a /= 2;
    n /= 2;
  }

  if(a % 2 == 0){
    out << -1;
    return;
  }

  int step = 0;
  while(n % 2 == 0){
    n /= 2;
    k *= 2;
    step++;
  }

  if (a % n != 0){
    out << -1;
    return;
  }

  int t = a / n;
  if(t > k / 2){
    out << -1;
    return;
  }

  out << step;
}

RUN_ONCE