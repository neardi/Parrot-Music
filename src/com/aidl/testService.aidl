package com.aidl;
interface testService {
  void UpnpPLAY();
  void UpnpPAUSE();
  void UpnpSEEK(int a);
  int UpnpGETDURATION();
  int UpnpGETDURATIONSECONDS();
  void UpnpSTOP();
  void UpnpSETURI(String b);

 
}