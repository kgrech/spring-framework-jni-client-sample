#include <fstream>

#include <jni.h>
#include "com_github_kgrech_statcollectior_client_monitor_CpuChecker.h"

JNIEXPORT jfloat JNICALL Java_com_github_kgrech_statcollectior_client_monitor_CpuChecker_doGetValue
  (JNIEnv *, jobject)
{
    float f;
    int i;
    char c;
    std::ifstream in("/proc/loadavg");
    //  0.39 0.38 0.40  2    /    869 15484
    in >> f >> f;
    return f / 100;
}

