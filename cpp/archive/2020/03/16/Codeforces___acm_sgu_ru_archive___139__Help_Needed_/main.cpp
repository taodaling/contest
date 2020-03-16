#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  vector<int> state(16);
  int dist = 0;
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      int x;
      in >> x;
      state[i * 4 + j] = x;
      if (x == 0) {
        dist = 3 - i + 3 - j;
      }
    }
  }

  int cnt = dist;
  for (int i = 0; i < 16; i++) {
    for (int j = 0; j < i; j++) {
      if (state[i] > state[j]) {
        cnt++;
      }
    }
  }

  if (cnt % 2 == 1) {
    out << "YES";
  } else {
    out << "NO";
  }
}

RUN_ONCE