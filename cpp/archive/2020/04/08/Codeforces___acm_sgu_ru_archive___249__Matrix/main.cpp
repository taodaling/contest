#include "../../libs/common.h"
#include "../../libs/gray_code.h"

using namespace gray_code;

void solve(int testId, istream &in, ostream &out) {
  int n;
  int m;
  in >> n >> m;
  int nmask = 1 << n;
  int mmask = 1 << m;

  stringstream ss;

  for (int i = 0; i < nmask; i++) {
    ui ti = (i ^ (i >> 1)) << m;
    for (int j = 0; j < mmask; j++) {
      ss << (ti | (j ^ (j >> 1))) << ' ';
    }
    ss << endl;
  }

  out << ss.str();
}

RUN_ONCE