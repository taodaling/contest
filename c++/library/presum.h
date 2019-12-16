//
// Created by DALT on 12/15/2019.
//

#ifndef JHELPER_EXAMPLE_PROJECT_PRESUM_H
#define JHELPER_EXAMPLE_PROJECT_PRESUM_H

#include "common.h"

namespace dalt {
    template<typename T>
    class presum {
    public:
        presum(int cap) : _vec(cap) {
        }

        presum() {
        }

        presum(const presum<T> &vec) : _vec(vec._vec), _n(vec._n) {
        }

        void populate(vector<T> vec) {
            _vec.begin();
            _vec.clear();
            _vec.insert(_vec.end(), vec.begin(), vec.end());
            _n = _vec.size();
            for (int i = 1; i < _n; i++) {
                _vec[i] += _vec[i - 1];
            }
        };

        T interval(int l, int r){
            if(l > r){
                return 0;
            }
            T ans = _vec[r];
            if(l > 0){
                ans -= _vec[l - 1];
            }
            return ans;
        }
    private:
        vector<T> _vec;
        int _n;
    };
}

#endif //JHELPER_EXAMPLE_PROJECT_PRESUM_H
