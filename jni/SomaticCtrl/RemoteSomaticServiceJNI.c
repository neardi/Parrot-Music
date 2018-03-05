/*
*	Copyright (C) 2012 SVA Technologies Co., Ltd	
*	@description£º	Rainbow Somatic Games Control Device JNI Operation Call.
*	@author			LVQINGXIAN.
*	@date			2012-08-28.
*/
#include <stdio.h>
#include <stdlib.h>
#include <linux/ioctl.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include "RemoteSomaticServiceJNI.h"
#include "SensorOperation.h"

JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteSomatic_SomaticServiceJNI_sensorDevOpen
  (JNIEnv *env, jclass jobj){
	return openDev();
}


JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteSomatic_SomaticServiceJNI_sensorDevIoctl
  (JNIEnv *env, jclass jobj, jint fd){
	return ioctlDev(fd);
}


JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteSomatic_SomaticServiceJNI_sensorDevWriteData
  (JNIEnv *env, jclass jobj, jint fd, jfloat sensorData_x, jfloat sensorData_y, jfloat sensorData_z){
	return writeDataToDev(fd,sensorData_x,sensorData_y,sensorData_z);
}

JNIEXPORT jint JNICALL Java_com_sva_rainbow_remote_RemoteSomatic_SomaticServiceJNI_sensorDevClose
  (JNIEnv *env, jclass jobj, jint fd){
	return closeDev(fd);
}
