#ifndef SPARSE_TABLE_H
#define SPARSE_TABLE_H

#include "common.h"
#include "bits.h"

namespace sparse_table
{
template <class T, class F = function<T(const T &, const T &)>>
class SparseTable
{
private:
    vector<vector<T>> _levels;
    int _n;
    function<T(const T &, const T &) _func;

public:
    void init(vector<T> &data, function<T(const T, const T) func){
        
    }
};
} // namespace sparse_table

#endif