#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int a, b;
  in >> a >> b;
  out << (a >= b && a % b == 0 ? "YES" : "NO") << endl;
}

RUN_MULTI