/*
*	Copyright (C) 2012 SVA Technologies Co., Ltd	
*	@description£º	Rainbow Somatic Games Control Device JNI Operation Implementation.
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
#include "sva_gsensor.h"
#include "SensorOperation.h"


int openDev(){
	int dev_fd = -1;
	dev_fd = open(SENSORDEV, O_RDWR);

	if(-1 == dev_fd){
		perror("SensorOperation,Open device failure : ");
	}
	return dev_fd;
}

int ioctlDev(int fd){
	int nRet = -1;
	nRet = ioctl(fd, ACC_VIRTUAL_ON, NULL);

	if(nRet){
		perror("SensorOperation,Ioctl failure :");
	}

	return nRet;
}

int writeDataToDev(int fd,float sensorData_x,float sensorData_y,float sensorData_z){
	int nRet = -1;
	struct accel_data sensorData;
	sensorData.x = (sensorData_x * 128) / (4*9.8) + 128;
	sensorData.y = (sensorData_y * 128) / (4*9.8) + 128;
	sensorData.z = (sensorData_z * 128) / (4*9.8) + 128;

	nRet = write(fd,(void *)&sensorData,sizeof(struct accel_data));
	if(sizeof(struct accel_data) != nRet){
		perror("SensorOperation,write data error : ");
		return -1;
	}
	return 0;
}

int closeDev(int fd){
	return close(fd);
}
