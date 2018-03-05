
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

LOCAL_MODULE    := InputDevicesOperation
LOCAL_SRC_FILES := InputDevicesOperation.c \
				   DeviceFile.c
LOCAL_PRELINK_MODULE := false
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)