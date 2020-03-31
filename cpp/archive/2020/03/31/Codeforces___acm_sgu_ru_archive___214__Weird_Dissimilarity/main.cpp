#include "../../libs/common.h"
#include "../../libs/debug.h"

vector<vector<ll>> penalties;
vector<ll> minPenaltiesA;
vector<int> minPenaltiesAChar;
vector<ll> minPenaltiesB;
vector<int> minPenaltiesBChar;
vector<int> a;
vector<int> b;
int n;
int m;
int charset;
vector<int> indexToChar;
vector<int> charToIndex;
vector<vector<ll>> dp;
vector<vector<pair<int, int>>> last;
ll inf = 1e18;

ll Dp(int i, int j) {
  if (i < 0 || j < 0) {
    return inf;
  }
  if (dp[i][j] == -1) {
    if (i == 0 && j == 0) {
      return dp[i][j] = 0;
    }
    dp[i][j] = inf;
    if (i > 0 && j > 0) {
      ll cand = Dp(i - 1, j - 1) + penalties[a[i - 1]][b[j - 1]];
      if (dp[i][j] > cand) {
        dp[i][j] = cand;
        last[i][j].first = last[i][j].second = -1;
      }
    }
    if (i > 0) {
      ll cand = Dp(i - 1, j) + minPenaltiesA[a[i - 1]];
      if (dp[i][j] > cand) {
        dp[i][j] = cand;
        last[i][j].first = -1;
        last[i][j].second = minPenaltiesAChar[a[i - 1]];
      }
    }
    if (j > 0) {
      ll cand = Dp(i, j - 1) + minPenaltiesB[b[j - 1]];
      if (dp[i][j] > cand) {
        dp[i][j] = cand;
        last[i][j].first = minPenaltiesBChar[b[j - 1]];
        last[i][j].second = -1;
      }
    }
  }
  return dp[i][j];
}

void solve(int testId, istream &in, ostream &out) {
  string cs;
  string as, bs;
  in >> cs;
  in >> as >> bs;
  n = as.size();
  m = bs.size();
  charset = cs.length();
  indexToChar.assign(cs.begin(), cs.end());
  charToIndex.resize(500);
  for (int i = 0; i < charset; i++) {
    charToIndex[indexToChar[i]] = i;
  }

  for (char &c : as) {
    a.push_back(charToIndex[c]);
  }
  for (char &c : bs) {
    b.push_back(charToIndex[c]);
  }
  dbg(a, b);

  penalties.resize(charset, vector<ll>(charset));
  minPenaltiesA.resize(charset, inf);
  minPenaltiesB.resize(charset, inf);
  minPenaltiesAChar.resize(charset);
  minPenaltiesBChar.resize(charset);
  for (int i = 0; i < charset; i++) {
    for (int j = 0; j < charset; j++) {
      in >> penalties[i][j];
      if (minPenaltiesA[i] > penalties[i][j]) {
        minPenaltiesA[i] = penalties[i][j];
        minPenaltiesAChar[i] = j;
      }
      if (minPenaltiesB[j] > penalties[i][j]) {
        minPenaltiesB[j] = penalties[i][j];
        minPenaltiesBChar[j] = i;
      }
    }
  }

  dbg(penalties, minPenaltiesA, minPenaltiesB, minPenaltiesAChar,
      minPenaltiesBChar);

  last.resize(n + 1, vector<pair<int, int>>(m + 1, {-1, -1}));
  dp.resize(n + 1, vector<ll>(m + 1, -1));

  ll ans = Dp(n, m);
  dbg(dp);
  out << ans << endl;
  vector<int> s1;
  vector<int> s2;

  int i = n;
  int j = m;
  while (true) {
    if (i == 0 && j == 0) {
      break;
    }
    int ni = i;
    int nj = j;
    if (last[i][j].first == -1) {
      s1.push_back(a[i - 1]);
      ni--;
    } else {
      s1.push_back(last[i][j].first);
    }
    if (last[i][j].second == -1) {
      s2.push_back(b[j - 1]);
      nj--;
    } else {
      s2.push_back(last[i][j].second);
    }
    i = ni;
    j = nj;
  }

  reverse(s1.begin(), s1.end());
  reverse(s2.begin(), s2.end());
  for (int c : s1) {
    out << (char)indexToChar[c];
  }
  out << endl;
  for (int c : s2) {
    out << (char)indexToChar[c];
  }
}

RUN_ONCE