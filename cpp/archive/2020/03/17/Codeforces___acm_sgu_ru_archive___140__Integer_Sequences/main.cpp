#include "../../libs/common.h"
#include "../../libs/modular.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  int p, b;
  in >> p >> b;

  vector<int> a(n + 1);
  for (int i = 0; i < n; i++) {
    in >> a[i];
  }
  a[n] = p;

  dbg(a);

  vector<int> coe(n + 1);
  int g = modular::Extgcd(a, coe, p);

  if (g == 0) {
    if(b != 0){
      out << "NO";
      return;
    }
    out << "YES" << endl;
    for(int i = 0; i < n; i++){
      out << 0 << ' ';
    }
    return;
  }

  if (b % g != 0) {
    out << "NO";
    return;
  }

  out << "YES" << endl;
  int prod = b / g;
  for (int i = 0; i < n; i++) {
    out << modular::Modmul(prod, coe[i], p) << ' ';
  }
}

RUN_ONCE