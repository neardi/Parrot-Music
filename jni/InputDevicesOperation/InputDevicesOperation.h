/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI */

#ifndef _Included_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
#define _Included_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
 * Method:    ColaTestJni
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaTestJni
  (JNIEnv *, jclass);

/*
 * Class:     com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
 * Method:    ColaOpenFile
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaOpenFile
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
 * Method:    ColaWriteFile
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaWriteFile
  (JNIEnv *, jclass, jint, jint, jint, jint);

/*
 * Class:     com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
 * Method:    ColaFileClose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaFileClose
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
 * Method:    insmod
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_insmod
  (JNIEnv *, jclass, jstring, jstring);

/*
 * Class:     com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI
 * Method:    rmmod
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_rmmod
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif