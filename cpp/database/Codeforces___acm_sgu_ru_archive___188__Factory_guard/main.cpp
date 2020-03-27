#include "../../libs/common.h"
#include "../../libs/modular.h"

void solve(int testId, istream &in, ostream &out) {
  int n, t;
  in >> n >> t;

  vector<int> position(n);
  vector<ll> speeds(n);
  vector<int> pos;
  vector<int> neg;
  for (int i = 0; i < n; i++) {
    in >> position[i];
  }
  for (int i = 0; i < n; i++) {
    in >> speeds[i];
    if (speeds[i] > 0) {
      pos.push_back(i);
    } else {
      neg.push_back(i);
    }
  }

  int m = 1000;
  vector<int> cnt(n);
  for (int posIndex : pos) {
    for (int negIndex : neg) {
      ll s = (speeds[posIndex] - speeds[negIndex]) * t;
      int d = modular::Mod(position[negIndex] - position[posIndex], m);
      if (s >= d) {
        int meet = (s - d) / m;
        cnt[posIndex] += 1 + meet;
        cnt[negIndex] += 1 + meet;
      }
    }
  }

  for(int i = 0; i < n; i++){
    out << cnt[i] << ' ';
  }
}

RUN_ONCE