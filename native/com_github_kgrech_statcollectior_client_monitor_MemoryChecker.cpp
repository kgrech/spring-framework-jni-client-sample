#include <fstream>

#include <jni.h>
#include "com_github_kgrech_statcollectior_client_monitor_MemoryChecker.h"

JNIEXPORT jfloat JNICALL Java_com_github_kgrech_statcollectior_client_monitor_MemoryChecker_doGetValue
        (JNIEnv *env, jobject obj)
{
    std::string s;
    unsigned int memTotal;
    unsigned int memFree;
    std::ifstream in("/proc/meminfo");
    in >> s >> memTotal >> s; //MemTotal:        8136960 kB
    in >> s >> memFree  >> s; //MemFree:         1153252 kB
    return ((float)(memTotal - memFree)) / (memTotal);
}