// INoticeService.aidl
package net.oschina.app.v2.service;

// Declare any non-default types here with import statements

interface INoticeService {
   void scheduleNotice();
   void requestNotice();
   void clearNotice(int uid,int type);
}
