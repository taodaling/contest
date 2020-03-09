#include "../../libs/common.h"
#include "../../libs/gcd.h"
#include "../../libs/decimal.h"
#include "../../libs/solver.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  ll simpleLimit = decimal::RoundToInt<ll>(n > 18 ? 1e18 : pow(10, n)) - 1;
  ll total = 0;
  int limit = 1e5;
  ll target = 987654321;
  ll mod = (ll)1e9;
  ll inf = 1e18;
  for (int i = 0; i < limit && i <= simpleLimit; i++) {
    ll c = target - (ll)i * i;
    ll a = (ll)limit * 2 * i;
    ll x1 = 0;
    ll x2 = min((ll)1e4 - 1, (simpleLimit - i) / limit);

    ll contrib = solver::CountWay(a, mod, c, x1, x2, -inf, inf);
    total += contrib;

    // if (contrib) {
    //   ll actual = 0;
    //   for(int j = 0; j < 1e4; j++){
    //     ll v = j * limit + i;
    //     if(v * v % mod == target){
    //         actual++;
    //     }
    //   }
    //   error(i, actual, contrib);
    // }
  }

  // for(int i = 0; i < mod; i++){
  //   if(((ll)i * i) % mod == target){
  //     error(i);
  //   }
  // }

  if (total == 0) {
    out << 0;
    return;
  }

  if (n <= 9) {
    out << total;
    for (int i = 9; i < n; i++) {
      out << '0';
    }
  }
  else{
    out << total * 9;
    for (int i = 10; i < n; i++) {
      out << '0';
    }
  }
}

RUN_ONCE