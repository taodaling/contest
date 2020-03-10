#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  int factor = 0;
  for (int i = 2; i * i <= n; i++) {
    while (n % i == 0) {
      n /= i;
      factor++;
    }
  }
  if(n != 1){
    factor++;
  }
  if (factor == 2) {
    out << "Yes";
  } else {
    out << "No";
  }

  out << endl;
}

RUN_MULTI