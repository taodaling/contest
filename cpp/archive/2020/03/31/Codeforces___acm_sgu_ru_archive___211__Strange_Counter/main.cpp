#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/version_array.h"

version_array::VersionArray<int, 10000> va;
vector<int> seq;
vector<int> counter(10000);

void Change(int i, int j) {
  counter[i] = j;
  if (!va[i]) {
    va[i] = 1;
    seq.push_back(i);
  }
}

void Print(ostream &out) {
  out << seq.size() << ' ';
  for (int x : seq) {
    out << x << ' ' << counter[x] << ' ';
  }
  out << endl;
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;

  for (int i = 0; i < m; i++) {
    int k;
    in >> k;
    seq.clear();
    va.clear();

    dbg(i, vector<int>(counter.begin(), counter.begin() + n));

    Change(k, counter[k] + 1);
    if (counter[k] == 3) {
      Change(k, 1);
      Change(k + 1, counter[k + 1] + 1);
      k++;
    }

    int pre = -1;
    for (int j = k + 1; j < n && counter[j] > 0; j++) {
      if (counter[j] == 2) {
        pre = j;
        break;
      }
    }

    if (pre == -1 && counter[k] == 2) {
      Change(k, 0);
      Change(k + 1, counter[k + 1] + 1);
    }
    if (pre >= 0) {
      Change(pre, 0);
      Change(pre + 1, counter[pre + 1] + 1);
    }

    Print(out);
  }
}

RUN_ONCE