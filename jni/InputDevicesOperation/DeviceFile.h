#ifndef _DEVICE_FILE_
#define _DEVICE_FILE_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <errno.h>
#include <unistd.h>
#include <malloc.h>
#include <android/log.h>

#define LOG_TAG "JNI_InputDevicesOperation"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define EVIOCGVERSION		_IOR('E', 0x01, int)

#define MAXOPENFILES            20
#define FILE_SUCCESS            0x0F000000
#define FILE_FAIL               0x0F000001
#define ERR_FILEOPEN_MUCH       0x0F000002
#define ERR_FILEOPEN_FAIL       0x0F000003
#define ERR_FILECLOSE_FAIL      0x0F000004

typedef unsigned char   UINT8;
typedef unsigned short  UINT16;
typedef char            INT8;
typedef short           INT16;
typedef int             INT;
typedef unsigned int    UINT;
typedef long            INT32;
typedef unsigned long   UINT32;
typedef unsigned long   DWORD;
typedef int             BOOL;
typedef struct{
//	FILE  *fp;
	int fp;
	UINT8 filename[256];
}FILEOPENED;

struct input_event {
	struct timeval time;
	__u16 type;
	__u16 code;
	__s32 value;
};
INT FileOpen(const char *pFileName);
INT32 FileWrite(INT fd, INT type , INT code ,INT value);
INT  FileClose(INT fd);
INT  insmod(const char *fileName,const char *argc);
INT  rmmod(const char *modname);
#endif

