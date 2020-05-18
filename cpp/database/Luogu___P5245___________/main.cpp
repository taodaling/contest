#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/modular.h"
#include "../../libs/poly.h"

const int mod = 998244353;
typedef algebra::modular<mod> base;
typedef algebra::poly<base> polyn;

void solve(int testId, istream &in, ostream &out) {
  base::invCache.resize(1000000);
  vector<int> inv(1000000);
  modular::InverseRange(inv, mod);
  for(int i = 0; i < 1000000; i++){
    base::invCache[i] = inv[i];
  }

  int n;
  base k;
  in >> n;
  string kstr;
  in >> kstr;
  for (char c : kstr) {
    k = k * base(10) + base(c - '0');
  }

  dbg(kstr, k);

  vector<base> coes(n);
  for (int i = 0; i < n; i++) {
    in >> coes[i];
  }

  polyn a(coes);
  polyn logp = a.log(n);
  logp.print();

  logp *= k;
  polyn expp = logp.exp(n);
  expp.print();

  for (int i = 0; i < n; i++) {
    out << expp[i] << ' ';
  }
}

RUN_ONCE