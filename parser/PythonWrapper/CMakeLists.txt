find_package(PythonLibs 3.9 REQUIRED)

include_directories(${PYTHON_INCLUDE_DIRS} ../Parser/)

add_library(caff SHARED PythonInterop.cpp)
set_target_properties(caff PROPERTIES SUFFIX ".so")
set_target_properties(caff PROPERTIES PREFIX "")

target_link_libraries(caff ${PYTHON_LIBRARIES})
