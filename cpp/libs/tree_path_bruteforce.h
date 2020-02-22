#ifndef TREE_PATH_BRUTEFORCE_H
#define TREE_PATH_BRUTEFORCE_H

#include "common.h"
#include "lca.h"

namespace tree_path_bruteforce
{
template <class T>
struct Node
{
    int r;
    int node;
    T data;
};

template <class T, int MAX_N>
class TreePathBF
{
public:
    void init(vector<vector<int>> &tree)
    {
        _seqTail = 0;
        _lca.init(tree, 0);
        dfs(0, -1, tree);
        compress(0, -1, tree);
    }

    void operator()(int u, int v,  const function<void(Node<T> &)> &consumer)
    {
        int lca = _lca(u, v);
        consumer(_seq[_nodeToSeq[lca]]);
        upToBottom(lca, u, consumer);
        upToBottom(lca, v, consumer);
    }


private:

   void upToBottom(int ancestor, int v, const function<void(Node<T> &)> &consumer)
    {
        int l = _nodeToSeq[v];
        int r = _seq[l].r;
        for (int i = _nodeToSeq[ancestor] + 1; i <= l; i++)
        {
            if (_seq[i].r >= l)
            {
                consumer(_seq[i]);
            }
            else
            {
                i = _seq[i].r;
            }
        }
    }

    void dfs(int root, int p, vector<vector<int>> &tree)
    {
        _size[root] = 1;
        for (int node : tree[root])
        {
            if (node == p)
            {
                continue;
            }
            dfs(node, root, tree);
            _size[root] += _size[node];
        }
        sort(tree[root].begin(), tree[root].end(), [this](auto &a, auto &b) { return _size[a] > _size[b]; });
    }

    void compress(int root, int p, vector<vector<int>> &tree)
    {
        int id = _seqTail++;
        _seq[id].r = id;
        _seq[id].node = root;
        _nodeToSeq[root] = id;
        for (int node : tree[root])
        {
            if (node == p)
            {
                continue;
            }
            compress(node, root, tree);
        }
        _seq[id].r = _seqTail - 1;
    }

    lca::Lca _lca;
    Node<T> _seq[MAX_N];
    int _nodeToSeq[MAX_N];
    int _size[MAX_N];
    int _seqTail;
};
}; // namespace tree_path_bruteforce

#endif