//
// Created by daltao on 2019/12/18.
//

#ifndef ACAUTOMATON_H
#define ACAUTOMATON_H

#include "common.h"

namespace dalt {
    template<int L, int R>
    class ACNode {
    public:
        ACNode *next[R - L + 1];
        ACNode *fail;
        ACNode *father;
        int index;
        int id;
        int cnt;
        int preSum;

        ACNode(){
            memset(next, 0, sizeof(next));
            fail = father = 0;
            index = id = cnt = preSum = 0;
        }

        int getId() {
            return id;
        }

        int getCnt() {
            return cnt;
        }

        void decreaseCnt() {
            cnt--;
        }

        void increaseCnt() {
            cnt++;
        }

        int getPreSum() {
            return preSum;
        }
    };

    template<int L, int R>
    class ACAutomaton {
    private:
        ACNode<L, R> *root;
        ACNode<L, R> *buildLast;
        ACNode<L, R> *matchLast;
        vector<ACNode<L, R> *> allNodes;

        ACNode<L, R> *addNode() {
            ACNode<L, R> *node = new ACNode<L, R>();
            node->id = allNodes.size();
            allNodes.push_back(node);
            return node;
        }

    public:

        ACNode<L, R> *getBuildLast() {
            return buildLast;
        }


        ACNode<L, R> *getMatchLast() {
            return matchLast;
        }


        vector<ACNode<L, R> *> getAllNodes() {
            return allNodes;
        }


        ACAutomaton() {
            root = buildLast = matchLast = 0;
            root = addNode();
        }

        void beginBuilding() {
            buildLast = root;
        }

        void endBuilding() {
            deque<ACNode<L, R> *> que;
            for (int i = 0; i < (R - L + 1); i++) {
                if (root->next[i] != NULL) {
                    que.push_back(root->next[i]);
                }
            }

            while (!que.empty()) {
                ACNode<L, R> *head = que.front();
                que.pop_front();
                ACNode<L, R> *fail = visit(head->father->fail, head->index);
                if (fail == NULL) {
                    head->fail = root;
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
                if (root->next[i] != NULL) {
                    que.push_back(root->next[i]);
                } else {
                    root->next[i] = root;
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
            if (buildLast->next[index] == NULL) {
                ACNode<L, R> *node = addNode();
                node->father = buildLast;
                node->index = index;
                buildLast->next[index] = node;
            }
            buildLast = buildLast->next[index];
        }


        void beginMatching() {
            matchLast = root;
        }


        void match(char c) {
            int index = c - L;
            matchLast = matchLast->next[index];
        }

    };
}

#endif //ACAUTOMATON_H
