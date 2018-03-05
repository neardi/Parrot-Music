
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

LOCAL_MODULE    := SensorDevOperation
LOCAL_SRC_FILES := SensorOperation.c \
				   RemoteSomaticServiceJNI.c
LOCAL_PRELINK_MODULE := false
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)