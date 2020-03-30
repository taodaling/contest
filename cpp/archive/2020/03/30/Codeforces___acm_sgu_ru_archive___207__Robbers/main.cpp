#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/util.h"

int Y, M;
int Eval(int x, int k) { return abs(x * M - k * Y); }

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n >> M >> Y;
  vector<int> x(n);
  vector<int> k(n);
  vector<int> f(n);

  for (int i = 0; i < n; i++) {
    in >> x[i];
    f[i] = (long double)(x[i] * M) / Y;
  }

  vector<int> indices = util::Range(0, n - 1);
  int remain = M;
  priority_queue<int, vector<int>, function<bool(int, int)>> pq(
      indices.begin(), indices.end(), [&](int a, int b) {
        return (Eval(x[a], k[a] + 1) - Eval(x[a], k[a])) >
               (Eval(x[b], k[b] + 1) - Eval(x[b], k[b]));
      });

  while (remain > 0) {
    int top = pq.top();
    pq.pop();
    int used = 1;
    if (f[top] > k[top]) {
      used = min(remain, f[top] - k[top]);
    } else if (f[top] == k[top]) {
      used = 1;
    } else {
      used = remain;
    }
    remain -= used;
    k[top] += used;
    pq.push(top);
  }

  for (int i = 0; i < n; i++) {
    out << k[i] << ' ';
  }
}

RUN_ONCE