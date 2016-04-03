#include <fstream>

#include <jni.h>
#include "com_github_kgrech_statcollectior_client_monitor_ProcessesChecker.h"

JNIEXPORT jint JNICALL Java_com_github_kgrech_statcollectior_client_monitor_ProcessesChecker_doGetValue
  (JNIEnv *, jobject)
{
    float f;
    unsigned int i;
    char c;
    std::ifstream in("/proc/loadavg");
    //   0.39 0.38 0.40  2    /    869 15484
    in >> f >> f >> f >> i >> c >> i;
    return i;
}