#ifndef VERSION_ARRAY_H
#define VERSION_ARRAY_H

#include "common.h"

namespace version_array
{
template <class T>
class VersionArray
{
private:
    vector<T> _data;
    vector<int> _version;
    T _def;
    int _now;
    void access(int i)
    {
        if (_version[i] != _now)
        {
            _version[i] = _now;
            _data[i] = _def;
        }
    }

public:
    VersionArray(int cap, const T &def = 0) : _data(cap), _version(cap), _def(def), _now(1)
    {
    }
    void clear()
    {
        _now++;
    }
    T get(int i)
    {
        access(i);
        return _data[i];
    }
    void set(int i, const T &val)
    {
        access(i);
        _data[i] = val;
    }
    void modify(int i, const T &val)
    {
        access(i);
        _data[i] += val;
    }
};
} // namespace ds

#endif