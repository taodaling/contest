#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int a, b, c, ca, cb, cc;
  in >> a >> b >> c >> ca >> cb >> cc;
  out << min(a / ca, min(b / cb, c / cc));
  
}

RUN_ONCE