import Foundation
import UserNotifications
import React

@objc(RnNotificationSetting)
class RnNotificationSetting: RCTEventEmitter {

  override init() {
    super.init()
    NotificationCenter.default.addObserver(self, selector: #selector(applicationWillEnterForeground), name: UIApplication.willEnterForegroundNotification, object: nil)
  }

  deinit {
    NotificationCenter.default.removeObserver(self)
  }

  @objc
  func applicationWillEnterForeground() {
    checkNotificationSettingsAndEmitEvent()
  }

  @objc
  func checkNotificationSettingsAndEmitEvent() {
    let center = UNUserNotificationCenter.current()
    center.getNotificationSettings { settings in
      var status: Int
      switch settings.authorizationStatus {
      case .notDetermined:
        status = -1
      case .denied:
        status = 0
      case .authorized:
        status = 1
      case .provisional:
        status = 2
      case .ephemeral:
        status = 3
      @unknown default:
        status = -1
      }
      self.sendEvent(withName: "NotificationSettingsChanged", body: ["status": status])
    }
  }

  override func supportedEvents() -> [String]! {
    return ["NotificationSettingsChanged"]
  }

  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
