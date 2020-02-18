#define MEM_LIMIT 888
#include "../../libs/common.h"
#include "../../libs/memory_pool.h"
#include "../../libs/no_tag_persistent_treap.h"

using PersistentTreap = no_tag_persistent_treap::NoTagPersistentTreap;

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n;
    vector<PersistentTreap *> versions;
    versions.push_back(PersistentTreap::NIL);
    for (int i = 1; i <= n; i++)
    {
        int v, opt, x;
        in >> v >> opt >> x;
        PersistentTreap *tree = PersistentTreap::Clone(versions[v]);
        if (opt == 1)
        {
            PersistentTreap *node = new PersistentTreap();
            node->_key = x;
            pair<PersistentTreap *, PersistentTreap *> p1 = PersistentTreap::SplitByKey(tree, x);
            tree = PersistentTreap::Merge(p1.first, node);
            tree = PersistentTreap::Merge(tree, p1.second);
        }
        else if (opt == 2)
        {
            pair<PersistentTreap *, PersistentTreap *> p1 = PersistentTreap::SplitByKey(tree, x);
            if (p1.first->_size > 0 && PersistentTreap::GetKeyByRank(p1.first, p1.first->_size) == x)
            {
                p1.first = PersistentTreap::SplitByRank(p1.first, p1.first->_size - 1).first;
            }
            tree = PersistentTreap::Merge(p1.first, p1.second);
        }
        else if (opt == 3)
        {
            pair<PersistentTreap *, PersistentTreap *> p1 = PersistentTreap::SplitByKey(tree, x - 1);
            out << p1.first->_size + 1 << endl;
            tree = PersistentTreap::Merge(p1.first, p1.second);
        }
        else if (opt == 4)
        {
            pair<PersistentTreap *, PersistentTreap *> p1 = PersistentTreap::SplitByRank(tree, x);
            out << PersistentTreap::GetKeyByRank(p1.first, p1.first->_size) << endl;
            tree = PersistentTreap::Merge(p1.first, p1.second);
        }
        else if (opt == 5)
        {
            pair<PersistentTreap *, PersistentTreap *> p1 = PersistentTreap::SplitByKey(tree, x - 1);
            if (p1.first->_size > 0)
            {
                out << PersistentTreap::GetKeyByRank(p1.first, p1.first->_size) << endl;
            }
            else
            {
                out << -(1LL << 31) + 1 << endl;
            }
            tree = PersistentTreap::Merge(p1.first, p1.second);
        }
        else if (opt == 6)
        {
            pair<PersistentTreap *, PersistentTreap *> p1 = PersistentTreap::SplitByKey(tree, x);
            if (p1.second->_size > 0)
            {
                out << PersistentTreap::GetKeyByRank(p1.second, 1) << endl;
            }
            else
            {
                out << (1LL << 31) - 1 << endl;
            }
            tree = PersistentTreap::Merge(p1.first, p1.second);
        }
        versions.push_back(tree);
    }
}

RUN_ONCE