#ifndef PUSH_RELABEL_H
#define PUSH_RELABEL_H

#include "common.h"

namespace push_relabel
{
template <int MAXN, class T = int>
struct HLPP
{
    const T INF = numeric_limits<T>::max();
    struct edge
    {
        int to, rev;
        T f;
    };
    int s = MAXN - 1, t = MAXN - 2;
    vector<edge> adj[MAXN];
    deque<int> lst[MAXN];
    vector<int> gap[MAXN];
    int ptr[MAXN];
    T excess[MAXN];
    int highest, height[MAXN], cnt[MAXN], work;
    void addEdge(int from, int to, int f, bool isDirected = true)
    {
        adj[from].push_back({to, adj[to].size(), f});
        adj[to].push_back({from, adj[from].size() - 1, isDirected ? 0 : f});
    }
    void updHeight(int v, int nh)
    {
        work++;
        if (height[v] != MAXN)
            cnt[height[v]]--;
        height[v] = nh;
        if (nh == MAXN)
            return;
        cnt[nh]++, highest = nh;
        gap[nh].push_back(v);
        if (excess[v] > 0)
            lst[nh].push_back(v), ptr[nh]++;
    }
    void globalRelabel()
    {
        work = 0;
        for (int i = 0; i < MAXN; i++)
        {
            height[i] = MAXN;
            cnt[i] = 0;
        }
        for (int i = 0; i <= highest; i++)
            lst[i].clear(), gap[i].clear(), ptr[i] = 0;
        height[t] = 0;
        queue<int> q({t});
        while (!q.empty())
        {
            int v = q.front();
            q.pop();
            for (auto &e : adj[v])
                if (height[e.to] == MAXN && adj[e.to][e.rev].f > 0)
                    q.push(e.to), updHeight(e.to, height[v] + 1);
            highest = height[v];
        }
    }
    void push(int v, edge &e)
    {
        if (excess[e.to] == 0)
            lst[height[e.to]].push_back(e.to), ptr[height[e.to]]++;
        T df = min(excess[v], e.f);
        e.f -= df, adj[e.to][e.rev].f += df;
        excess[v] -= df, excess[e.to] += df;
    }
    void discharge(int v)
    {
        int nh = MAXN;
        for (auto &e : adj[v])
        {
            if (e.f > 0)
            {
                if (height[v] == height[e.to] + 1)
                {
                    push(v, e);
                    if (excess[v] <= 0)
                        return;
                }
                else
                    nh = min(nh, height[e.to] + 1);
            }
        }
        if (cnt[height[v]] > 1)
            updHeight(v, nh);
        else
        {
            for (int i = height[v]; i < MAXN; i++)
            {
                for (auto j : gap[i])
                    updHeight(j, MAXN);
                gap[i].clear(), ptr[i] = 0;
            }
        }
    }
    T calc(int src, int sink, int heur_n = MAXN)
    {
        s = src;
        t = sink;
        memset(excess, 0, sizeof(excess));
        excess[s] = INF, excess[t] = -INF;
        globalRelabel();
        for (auto &e : adj[s])
            push(s, e);
        for (; highest >= 0; highest--)
        {
            // while (ptr[highest] >= 0) {
            while (!lst[highest].empty())
            {
                // int v = lst[highest][ptr[highest]];
                // cout << highest << ' ' << ptr[highest] << ' ' << lst[highest].size() << endl;
                int v = lst[highest].back();
                // ptr[highest]--;
                lst[highest].pop_back();
                discharge(v);
                if (work > 4 * heur_n)
                    globalRelabel();
            }
        }
        return excess[t] + INF;
    }
};
} // namespace push_relabel

#endif