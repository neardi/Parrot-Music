#include "InputDevicesOperation.h"
#include "DeviceFile.h"


//#define DEBUG
JNIEXPORT jstring JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaTestJni
  (JNIEnv *env, jclass jobj){
#ifdef DEBUG
//	  LOGI("----> ColaMultiTouch JNI test <---- ");
#endif
      return (*env)->NewStringUTF(env, "----> ColaMultiTouch JNI test <---- ");
  }


JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaOpenFile
  (JNIEnv *env, jclass jobj, jstring pFileName){
		UINT8 *pbyFileName = (UINT8 *)(*env)->GetStringUTFChars(env, pFileName, 0);
#ifdef DEBUG
//		LOGI("ColaOpenFile --- > %s" , pbyFileName);
#endif
		return FileOpen(pbyFileName);
  }
  
  
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaWriteFile
  (JNIEnv *env, jclass jobj, jint fp,jint type, jint code, jint value){
#ifdef DEBUG
 // 		LOGI("ColaWriteFile --- > %d %d %d %d" , fp,type,code,value);
#endif
		return FileWrite(fp,type,code,value);
  }


JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_ColaFileClose
  (JNIEnv *env, jclass jobj, jint fp){
		return FileClose(fp);
  }
  
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_insmod
  (JNIEnv *env, jclass jobj, jstring filename, jstring argc){
		return insmod(((UINT8 *)(*env)->GetStringUTFChars(env, filename, 0)),((UINT8 *)(*env)->GetStringUTFChars(env, argc, 0)));
  }
  
JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteMouse_RemoteMouseJNI_rmmod
  (JNIEnv *env, jclass jobj, jstring modname){
		return rmmod(((UINT8 *)(*env)->GetStringUTFChars(env, modname, 0)));
  }
