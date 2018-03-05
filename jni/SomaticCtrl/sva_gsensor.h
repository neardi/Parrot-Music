#ifndef __SVA_GSENSOR_H__
#define __SVA_GSENSOR_H__
#include <linux/ioctl.h>

/*
** struct accel_data
** The accelermeter's data struct
*/
struct accel_data {
	char x;
	char y;
	char z;
};


/*
** ioctl: ACC_VIRTUAL_ON
** The Android accelermeter subsystem use
** user speace's data
*/
#define ACC_VIRTUAL_ON _IO('A', 1)

/*
** ioctl: ACC_VIRTUAL_OFF
** The Android accelermeter subsystem use
** peripheral device's data
*/
#define ACC_VIRTUAL_OFF _IO('A', 2)

#endif
