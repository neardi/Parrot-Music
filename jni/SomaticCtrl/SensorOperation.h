/*
*	Copyright (C) 2012 SVA Technologies Co., Ltd	
*	@description£º	Rainbow Somatic Games Control Device Operation header.
*	@author			LVQINGXIAN.
*	@date			2012-08-28.
*/
#ifndef __SENSOROPERATION_H__
#define __SENSOROPERATION_H__
#ifdef __cplusplus
extern "C" {
#endif

#define SENSORDEV "/dev/sva_gsensor"

/*
*	@description :open devices.
*	@parameter: void
*	@return : 0 if success,-1 if failure.
*/
int openDev();

/*
*	@description :ioctl devices.
*	@parameter: The device's file descriptor you wanto ctrl
*	@return : 0 if success,-1 if failure.
*/
int ioctlDev(int fd);

/*
*	@description :write data to devices.
*	@parameter: The device's file descriptor which you want to write to,and sensor data :x,y,z
*	@return : 0 if success,-1 if failure.
*/
int writeDataToDev(int fd,float sensorData_x,float sensorData_y,float sensorData_z);

/*
*	@description :close devices.
*	@parameter: The device's file descriptor which you wanto close
*	@return : 0 if success,-1 if failure.
*/
int closeDev(int fd);

#ifdef __cplusplus
}
#endif
#endif
