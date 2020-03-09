#ifndef ACAUTOMATON_H
#define ACAUTOMATON_H

#include "common.h"

namespace ac_automaton {
template <int L, int R>
class ACNode {
 public:
  ACNode *next[R - L + 1];
  ACNode *fail;
  ACNode *father;
  int index;
  int id;
  int cnt;
  int preSum;

  ACNode() {
    memset(next, 0, sizeof(next));
    fail = father = 0;
    index = id = cnt = preSum = 0;
  }

  int getId() { return id; }

  int getCnt() { return cnt; }

  void decreaseCnt() { cnt--; }

  void increaseCnt() { cnt++; }

  int getPreSum() { return preSum; }
};

template <int L, int R>
class ACAutomaton {
 public:
  ACNode<L, R> *_root;
  ACNode<L, R> *_buildLast;
  ACNode<L, R> *_matchLast;
  vector<ACNode<L, R> *> _allNodes;

  ACNode<L, R> *addNode() {
    ACNode<L, R> *node = new ACNode<L, R>();
    node->id = _allNodes.size();
    _allNodes.push_back(node);
    return node;
  }

 public:
  ACNode<L, R> *getBuildLast() { return _buildLast; }

  ACNode<L, R> *getMatchLast() { return _matchLast; }

  vector<ACNode<L, R> *> &getAllNodes() { return _allNodes; }

  ACAutomaton() {
    _root = _buildLast = _matchLast = 0;
    _root = addNode();
  }

  void beginBuilding() { _buildLast = _root; }

  void endBuilding() {
    deque<ACNode<L, R> *> que;
    for (int i = 0; i < (R - L + 1); i++) {
      if (_root->next[i] != NULL) {
        que.push_back(_root->next[i]);
      }
    }

    while (!que.empty()) {
      ACNode<L, R> *head = que.front();
      que.pop_front();
      ACNode<L, R> *fail = visit(head->father->fail, head->index);
      if (fail == NULL) {
        head->fail = _root;
      } else {
        head->fail = fail->next[head->index];
      }
      head->preSum = head->cnt + head->fail->preSum;
      for (int i = 0; i < (R - L + 1); i++) {
        if (head->next[i] != NULL) {
          que.push_back(head->next[i]);
        }
      }
    }

    for (int i = 0; i < (R - L + 1); i++) {
      if (_root->next[i] != NULL) {
        que.push_back(_root->next[i]);
      } else {
        _root->next[i] = _root;
      }
    }
    while (!que.empty()) {
      ACNode<L, R> *head = que.front();
      que.pop_front();
      for (int i = 0; i < (R - L + 1); i++) {
        if (head->next[i] != NULL) {
          que.push_back(head->next[i]);
        } else {
          head->next[i] = head->fail->next[i];
        }
      }
    }
  }

  ACNode<L, R> *visit(ACNode<L, R> *trace, int index) {
    while (trace != NULL && trace->next[index] == NULL) {
      trace = trace->fail;
    }
    return trace;
  }

  void build(char c) {
    int index = c - L;
    if (_buildLast->next[index] == NULL) {
      ACNode<L, R> *node = addNode();
      node->father = _buildLast;
      node->index = index;
      _buildLast->next[index] = node;
    }
    _buildLast = _buildLast->next[index];
  }

  void beginMatching() { _matchLast = _root; }

  void match(char c) {
    int index = c - L;
    _matchLast = _matchLast->next[index];
  }
};
}  // namespace ac_automaton

#endif  // ACAUTOMATON_H
