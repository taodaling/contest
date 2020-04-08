#ifndef KM_ALGO_H
#define KM_ALGO_H

#include "common.h"

namespace km_algo {
/**
 * http://longrm.com/2018/05/05/2018-05-05-KM/
 */
template <class V>
class KMAlgo {
 private:
  const vector<vector<V>> &table;  // 权重矩阵（方阵）
  vector<V> leftLabel;                    // X标号值
  vector<V> rightLabel;                    // Y标号值
  vector<int> leftPartner;              // X点对应的匹配点
  vector<int> rightPartner;              // Y点对应的匹配点
  int n;                           // 矩阵维度
  V INF;

 public:
  KMAlgo(const vector<vector<V>> &t)
      : table(t),
        INF(numeric_limits<V>::max() / 2),
        leftLabel(t.size(), -INF),
        rightLabel(t.size()),
        leftPartner(t.size(), -1),
        rightPartner(t.size(), -1),
        n(table.size()) {
    for (int x = 0; x < n; x++) {
      for (int y = 0; y < n; y++) {
        if (table[x][y] > leftLabel[x]) {
          leftLabel[x] = table[x][y];
        }
      }
    }
  }

  int getLeftPartner(int i) const { return leftPartner[i]; }

  int getRightPartner(int i) const { return rightPartner[i]; }

  V getLeftLabel(int i) const { return leftLabel[i]; }

  V getRightLabel(int i) const { return rightLabel[i]; }

  V solve() {  // 入口，输入权重矩阵
    for (int x = 0; x < n; x++) {
      bfs(x);
    }
    V value = 0;
    for (int x = 0; x < n; x++) {
      value += table[x][leftPartner[x]];
    }
    return value;
  }

 private:
  void bfs(int startX) {  // 为一个x点寻找匹配
    bool find = false;
    int endY = -1;
    vector<int> yPre(n, -1);   // 标识搜索路径上y点的前一个点
    vector<bool> S(n), T(n);   // S集合，T集合
    vector<V> slackY(n, INF);  // Y点的松弛变量
    vector<int> que(n);        // 队列
    int qs = 0, qe = 0;        // 队列开始结束索引
    que[qe++] = startX;
    while (!find) {               // 循环直到找到匹配
      while (qs < qe && !find) {  // 队列不为空
        int x = que[qs++];
        S[x] = true;
        for (int y = 0; y < n; y++) {
          if (T[y]) {
            continue;
          }
          V tmp = leftLabel[x] + rightLabel[y] - table[x][y];
          if (tmp == 0) {  // 相等子树中的边
            T[y] = true;
            yPre[y] = x;
            if (rightPartner[y] == -1) {
              endY = y;
              find = true;
              break;
            } else {
              que[qe++] = rightPartner[y];
            }
          } else if (slackY[y] >
                     tmp) {  // 不在相等子树中的边，看是否能够更新松弛变量
            slackY[y] = tmp;
            yPre[y] = x;
          }
        }
      }
      if (find) {
        break;
      }
      V a = INF;
      for (int y = 0; y < n; y++) {  // 找到最小的松弛值
        if (!T[y]) {
          a = min(a, slackY[y]);
        }
      }
      for (int i = 0; i < n; i++) {  // 根据a修改标号值
        if (S[i]) {
          leftLabel[i] -= a;
        }
        if (T[i]) {
          rightLabel[i] += a;
        }
      }
      qs = qe = 0;
      for (int y = 0; y < n; y++) {  // 重要！！！控制修改标号之后需要检查的x点
        if (!T[y] &&
            slackY[y] ==
                a) {  // 查看那些y点新加入到T集合，注意，这些y点的前向x点都记录在了yPre里面，所以这些x点不用再次入队
          T[y] = true;
          if (rightPartner[y] == -1) {  // 新加入的y点没有匹配，那么就找到可扩路了
            endY = y;
            find = true;
            break;
          } else {  // 新加入的y点已经有匹配了，将它匹配的x加到队列
            que[qe++] = rightPartner[y];
          }
        }
        slackY[y] -=
            a;  // 所有松弛值减去a。(对于T集合中的松弛值已经没用了，对于不在T集合里面的y点，
      }  // 它们的松弛值是通过S集合中的x点求出的，S集合中的x点的标号值在上面都减去了a，所以这里松弛值也要减去a)
    }
    while (endY != -1) {  // 找到可扩路最后的y点后，回溯并扩充
      int preX = yPre[endY], preY = leftPartner[preX];
      leftPartner[preX] = endY;
      rightPartner[endY] = preX;
      endY = preY;
    }
  }

  template <class T>
  friend ostream &operator<<(ostream &os, const KMAlgo<T> &algo);
};

template <class T>
ostream &operator<<(ostream &os, const KMAlgo<T> &algo) {
  os << "X: ";
  for (int i = 0; i < algo.n; i++) {
    os << algo.leftLabel[i] << ' ';
  }
  os << endl;
  os << "Y: ";
  for (int i = 0; i < algo.n; i++) {
    os << algo.rightLabel[i] << ' ';
  }
  os << endl;
  for(int i = 0; i < algo.n; i++){
    os << i << ' ' << algo.getLeftPartner(i) << endl;
  }
  return os;
}
}  // namespace km_algo

#endif