#ifndef PERSISTENT_TREAP_H
#define PERSISTENT_TREAP_H

#include "common.h"

namespace persistent_treap {
class PersistentTreap {
 private:
  PersistentTreap(bool x) {
    PersistentTreap();
    _left = this;
    _right = this;
    _size = 0;
  }

 public:
  PersistentTreap() {
    _left = NIL;
    _right = NIL;
    _size = 1;
    _key = 0;
  }

  static PersistentTreap *NIL;
  PersistentTreap *_left;
  PersistentTreap *_right;
  int _size;
  int _key;

  void pushDown() {
    if (this == NIL) {
      return;
    }
    _left = Clone(_left);
    _right = Clone(_right);
  }

  void pushUp() {
    if (this == NIL) {
      return;
    }
    _size = _left->_size + _right->_size + 1;
  }

  /**
   * split by rank and the node whose rank is argument will stored at result[0]
   */
  static pair<PersistentTreap *, PersistentTreap *> SplitByRank(
      PersistentTreap *root, int rank) {
    if (root == NIL) {
      return mp(NIL, NIL);
    }
    root->pushDown();
    pair<PersistentTreap *, PersistentTreap *> result;
    if (root->_left->_size >= rank) {
      result = SplitByRank(root->_left, rank);
      root->_left = result.second;
      result.second = root;
    } else {
      result =
          SplitByRank(root->_right, rank - (root->_size - root->_right->_size));
      root->_right = result.first;
      result.first = root;
    }
    root->pushUp();
    return result;
  }

  static PersistentTreap *Merge(PersistentTreap *a, PersistentTreap *b) {
    if (a == NIL) {
      return b;
    }
    if (b == NIL) {
      return a;
    }
    if (uniform_int_distribution<int>(1, a->_size + b->_size)(rng) <=
        a->_size) {
      a->pushDown();
      a->_right = Merge(a->_right, b);
      a->pushUp();
      return a;
    } else {
      b->pushDown();
      b->_left = Merge(a, b->_left);
      b->pushUp();
      return b;
    }
  }

  static PersistentTreap *DeepClone(const PersistentTreap *root) {
    if (root == NIL) {
      return NIL;
    }
    PersistentTreap *cl = new PersistentTreap(*root);
    cl->_left = DeepClone(cl->_left);
    cl->_right = DeepClone(cl->_right);
    return cl;
  }

  static PersistentTreap *Clone(const PersistentTreap *root) {
    if (root == NIL) {
      return NIL;
    }
    return new PersistentTreap(*root);
  }

  /**
   * nodes with key <= arguments will stored at result[0]
   */
  static pair<PersistentTreap *, PersistentTreap *> SplitByKey(
      PersistentTreap *root, int key) {
    if (root == NIL) {
      return mp(NIL, NIL);
    }
    root->pushDown();
    pair<PersistentTreap *, PersistentTreap *> result;
    if (root->_key > key) {
      result = SplitByKey(root->_left, key);
      root->_left = result.second;
      result.second = root;
    } else {
      result = SplitByKey(root->_right, key);
      root->_right = result.first;
      result.first = root;
    }
    root->pushUp();
    return result;
  }

  static int GetKeyByRank(PersistentTreap *treap, int k) {
    while (treap->_size > 1) {
      treap->pushDown();
      if (treap->_left->_size >= k) {
        treap = treap->_left;
      } else {
        k -= treap->_left->_size;
        if (k == 1) {
          break;
        }
        k--;
        treap = treap->_right;
      }
    }
    return treap->_key;
  }
};

PersistentTreap *PersistentTreap::NIL = new PersistentTreap(false);

void dfs(ostream &out, PersistentTreap *root) {
  if (root == PersistentTreap::NIL) {
    return;
  }
  root->pushDown();
  dfs(out, root->_left);
  out << root->_key << ',';
  dfs(out, root->_right);
}
ostream &operator<<(ostream &out, const PersistentTreap *root) {
  dfs(out, PersistentTreap::DeepClone(root));
  return out;
}
}  // namespace persistent_treap

#endif