#import "NotificationPermission.h"

@implementation NotificationPermission

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(hasPermission:(RCTResponseSenderBlock)callback) {
  float systemVersion = [[UIDevice currentDevice].systemVersion floatValue];
  
  
  if (systemVersion >= 8.0) {
    UIUserNotificationSettings *settings = [[UIApplication sharedApplication] currentUserNotificationSettings];
    UIUserNotificationType type = settings.types;
    if (type == UIUserNotificationTypeNone) {
      callback(@[@(NO)]);
    } else {
      callback(@[@(YES)]);
    }
    
  } else if (systemVersion >= 10.0) {
    [[UNUserNotificationCenter currentNotificationCenter] getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
      
      switch (settings.authorizationStatus)
      {
        case UNAuthorizationStatusDenied:
        case UNAuthorizationStatusNotDetermined:
          callback(@[@(NO)]);
          break;
        case UNAuthorizationStatusAuthorized:
          callback(@[@(YES)]);
          break;
      }
    }];
  }
}
@end
