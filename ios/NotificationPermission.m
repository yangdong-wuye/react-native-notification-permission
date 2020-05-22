#import "NotificationPermission.h"

#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif

@implementation NotificationPermission

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(hasPermission,
                 hasPermissionWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
  float systemVersion = [[UIDevice currentDevice].systemVersion floatValue];
  
  
  if (systemVersion >= 8.0) {
    UIUserNotificationSettings *settings = [[UIApplication sharedApplication] currentUserNotificationSettings];
    UIUserNotificationType type = settings.types;
    if (type == UIUserNotificationTypeNone) {
      resolve(@[@(NO)]);
    } else {
      resolve(@[@(YES)]);
    }
    
  } else if (systemVersion >= 10.0) {
    [[UNUserNotificationCenter currentNotificationCenter] getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
      
      switch (settings.authorizationStatus)
      {
        case UNAuthorizationStatusDenied:
        case UNAuthorizationStatusNotDetermined:
          resolve(@[@(NO)]);
          break;
        case UNAuthorizationStatusAuthorized:
          resolve(@[@(YES)]);
          break;
      }
    }];
  }
  
  
}
@end
