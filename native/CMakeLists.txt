cmake_minimum_required(VERSION 3.0)
project(stat_collector_native)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ../java/client/)

FILE(GLOB SOURCE_FILES *.cpp)

add_library(stat_collector_native MODULE ${SOURCE_FILES})
set_target_properties(stat_collector_native PROPERTIES SUFFIX ".so")
