cmake_minimum_required(VERSION 2.8.4)
project(jhelper-example-project)

add_executable(output output/main.cpp library/min_cost_max_flow.h library/assignment_problem.h library/bit.h library/sparse_segment.h library/segment.h library/gcd.h library/crt.h library/debug.h)
add_executable(testrunner testrunner/main.cpp)
file(GLOB TASKS "tasks/*.cpp")
file(GLOB_RECURSE HEADERS "library/*.h*" )

ADD_DEFINITIONS(-std=c++11)

add_executable(test ${TASKS} ${HEADERS})
