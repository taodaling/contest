#include "../../libs/common.h"

const int MAX_P = 50000 + 10;

int cnt[MAX_P];

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  int sum = 0;
  for (int i = 0; i < n; i++) {
    int x, p;
    in >> x >> p;
    cnt[x] += p;
    sum += p;
  }

  int pre = 0;
  for (int i = 0; i < MAX_P; i++) {
    pre += cnt[i];
    if (pre * 2 >= sum) {
      out << i << endl;
      return;
    }
  }
}

RUN_ONCE