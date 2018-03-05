#include "DeviceFile.h"

static FILEOPENED         fileopened[MAXOPENFILES] = {0};
static INT                g_nOpenFiles             = 0;

extern int init_module(void *, unsigned long, const char *);
extern int delete_module(const char *, unsigned int);


static void *read_file(const char *filename, ssize_t *_size)
{
	int ret, fd;
	struct stat sb;
	ssize_t size;
	void *buffer = NULL;

	/* open the file */
	fd = open(filename, O_RDONLY);
	if (fd < 0)
		return NULL;

	/* find out how big it is */
	if (fstat(fd, &sb) < 0)
		goto bail;
	size = sb.st_size;

	/* allocate memory for it to be read into */
	buffer = malloc(size);
	if (!buffer)
		goto bail;

	/* slurp it into our buffer */
	ret = read(fd, buffer, size);
	if (ret != size)
		goto bail;

	/* let the caller know how big it is */
	*_size = size;

bail:
	close(fd);
	return buffer;
}

INT insmod(const char *fileName,const char *argc)
{
	INT ret;

	void *file;
	ssize_t size = 0;
	char opts[10];//1024

	file = read_file(fileName, &size);
	if (!file) {
		return -1;
	}
	opts[0] = '\0';
	ret = init_module(file, size, opts);
	free(file);
	return ret;
}

INT rmmod(const char *modname)
{
	int ret = -1;
	char *str, *dot;
	
	str = strrchr(modname, '/');
	if (!str)
		str = modname;
	dot = strchr(modname, '.');
	if (dot)
		*dot = '\0';
		
	ret = delete_module(str, O_NONBLOCK | O_EXCL);
	return ret;
}

INT FileOpen(const char *pFileName)
{
	//FILE *fp = NULL;
	int fp;
    int  i = 0;

	if(g_nOpenFiles >= MAXOPENFILES)
	{
		return ERR_FILEOPEN_MUCH;
	}
	
//	fp = fopen(pFileName,"r+w");
	fp = open(pFileName, O_RDWR|O_NONBLOCK);


//	if (fp == NULL)
	if(fp < 0)
	{
		return ERR_FILEOPEN_FAIL;
	}
	else
	{
        for (i = 0; i < MAXOPENFILES; i ++)
        {
           // if ( NULL == fileopened[i].fp )
			if( 0 == fileopened[i].fp)
            {
                break;
            }
        }
		fileopened[i].fp = fp;
		strcpy(fileopened[i].filename,pFileName);
		g_nOpenFiles++;

		return (INT)fp;
	}
}

INT32 FileWrite(INT fd, INT type , INT code ,INT value)
{	
    int ret; 
    struct input_event event;

    memset(&event, 0, sizeof(event));
	event.type = (UINT16)type;
    event.code = (UINT16)code;
    event.value = value;

//	return fwrite(&event,sizeof(event),1,(FILE*)fd);
	ret = write(fd, &event, sizeof(event));
//	sync();
//	fsync(fd);
	return ret;
/*	if(ret < sizeof(event)) {
		return -1;
	}
	return 0; 
*/	
}

INT  FileClose(INT fd)
{
    int i  = 0;
    int ns = 0;

    ns = fd;

	if(fd != 0)
    {
		//if(fclose((FILE*)fd) == 0)
		if(close(fd) == 0)
        {
            for ( i = 0; i < 20; i ++)
            {
               // if ( fileopened[i].fp == (FILE *)ns )
			    if ( fileopened[i].fp == ns )
                {
                    break;
                }
            }
            memset(&fileopened[i], 0, sizeof( FILEOPENED));
			g_nOpenFiles--;
			return FILE_SUCCESS;
		}
		else
        {
			return ERR_FILECLOSE_FAIL;
		}
	}
	else{
		return FILE_FAIL;
	}
}

/*
static int writeDataToInputDeviceTest(int type,int code,int value)
{
    int i;
    int fd;
    int ret;
    int version;
    struct input_event event;

    fd = open("/dev/input/event4", O_RDWR);
    if(fd < 0) {
        return 1;
    }
    if (ioctl(fd, EVIOCGVERSION, &version)) {
        return 1;
    }
    memset(&event, 0, sizeof(event));

	event.type = type;
    event.code = code;
    event.value = value;
    ret = write(fd, &event, sizeof(event));
    if(ret < sizeof(event)) {
        fprintf(stderr, "write event failed, %s\n", strerror(errno));
        return -1;
    }
    return 0;
}
*/

