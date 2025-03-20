#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTLog.h>

@interface RCT_EXTERN_MODULE(RnNotificationSetting, RCTEventEmitter)

RCT_EXTERN_METHOD(checkNotificationSettingsAndEmitEvent)

@end
