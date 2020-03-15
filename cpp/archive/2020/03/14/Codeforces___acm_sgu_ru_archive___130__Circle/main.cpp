#include "../../libs/common.h"
#include "../../libs/reader.h"

vector<ll> ways;

void solve(int testId, istream &in, ostream &out) {
  reader::Input input(in);
  int k = input.ri();
  ways.resize(k + 1);

  ways[0] = 1;
  for (int i = 1; i <= k; i++) {
    for (int j = 0; j + 1 <= i; j++) {
      ways[i] += ways[j] * ways[i - 1 - j];
    }
  }

  out << ways[k] << ' ' << k + 1;
}

RUN_ONCE