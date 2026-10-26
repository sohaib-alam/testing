#include <jni.h>
#include <stdio.h>
#include <string.h>

JNIEXPORT void JNICALL Java_lab_NativeOverflow_copyInput(
    JNIEnv *env, jclass clazz, jstring input) {
    (void)clazz;
    const char *text = (*env)->GetStringUTFChars(env, input, NULL);
    if (text == NULL) return;

    char buffer[16];
    // CWE-120 / CWE-121: no size check; input can overwrite the stack buffer.
    strcpy(buffer, text);
    puts(buffer);

    (*env)->ReleaseStringUTFChars(env, input, text);
}
