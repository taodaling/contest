#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/decimal.h"
#include "../../libs/hash.h"
#include "../../libs/version_array.h"

using decimal::Merge;

int va[3][5];
hash::HashData<30, 31> hd31;
hash::HashData<30, 61> hd61;
hash::RollingHash<30, 30, 31> rh31(hd31);
hash::RollingHash<30, 30, 61> rh61(hd61);

int Mex(int i) {
  int ans = 0;
  while (va[i][ans]) {
    ans++;
  }
  return ans;
}

int Sg(vector<int> &sg, int pBegin, int pLen, ll n) {
  if (n >= pBegin) {
    n = pBegin + (n - pBegin) % pLen;
  }
  return sg[n];
}

void solve(int testId, istream &in, ostream &out) {
  int n, x, y, z;
  in >> n >> x >> y >> z;
  vector<vector<int>> sg(3);
  rh31.reset();
  rh61.reset();
  unordered_map<ll, int, hash::CustomHash> umap;
  for (int i = 0; i < 3; i++) {
    sg[i].push_back(0);
  }
  for (int i = 1; i < 5; i++) {
    C0(va);
    for (int j = 0; j < 3; j++) {
      va[j][sg[0][max(0, i - x)]]++;
    }
    va[0][sg[1][max(0, i - y)]]++;
    va[2][sg[1][max(0, i - y)]]++;
    va[0][sg[2][max(0, i - z)]]++;
    va[1][sg[2][max(0, i - z)]]++;

    for (int j = 0; j < 3; j++) {
      sg[j].push_back(Mex(j));
    }
  }

  for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 3; j++) {
      rh31.push(sg[j][i]);
      rh61.push(sg[j][i]);
    }
  }

  int periodBegin = -1;
  int periodLength = -1;
  for (int i = 5;; i++) {
    ll key = Merge(rh31.hash(), rh61.hash());
    if (umap.find(key) != umap.end()) {
      periodBegin = umap[key];
      periodLength = i - periodBegin;
      break;
    }
    umap[key] = i;

    for (int j = 0; j < 3; j++) {
      rh31.pop();
      rh61.pop();
    }

    C0(va);
    for (int j = 0; j < 3; j++) {
      va[j][sg[0][i - x]]++;
    }
    va[0][sg[1][i - y]]++;
    va[2][sg[1][i - y]]++;
    va[0][sg[2][i - z]]++;
    va[1][sg[2][i - z]]++;

    for (int j = 0; j < 3; j++) {
      sg[j].push_back(Mex(j));
      rh31.push(sg[j][i]);
      rh61.push(sg[j][i]);
    }
  }

  dbg(periodBegin, periodLength);
  dbg(sg);
  int sum = 0;
  vector<ll> a(n);
  for (int i = 0; i < n; i++) {
    in >> a[i];
    sum ^= Sg(sg[0], periodBegin, periodLength, a[i]);
  }

  int ans = 0;
  for (int i = 0; i < n; i++) {
    int tmp = sum ^ Sg(sg[0], periodBegin, periodLength, a[i]);
    if ((tmp ^ Sg(sg[0], periodBegin, periodLength, max<ll>(0, a[i] - x))) ==
        0) {
      ans++;
    }
    if ((tmp ^ Sg(sg[1], periodBegin, periodLength, max<ll>(0, a[i] - y))) ==
        0) {
      ans++;
    }
    if ((tmp ^ Sg(sg[2], periodBegin, periodLength, max<ll>(0, a[i] - z))) ==
        0) {
      ans++;
    }
  }

  out << ans << endl;
}

RUN_MULTI