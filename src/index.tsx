import {useEffect, useState} from 'react';
import {
  NativeModules,
  NativeEventEmitter,
  Platform,
  DeviceEventEmitter,
} from 'react-native';
import {FirebaseMessagingTypes} from '@react-native-firebase/messaging';

const LINKING_ERROR =
  "The package 'rn-notification-setting' doesn't seem to be linked. Make sure: \n\n" +
  Platform.select({ios: "- You have run 'pod install'\n", default: ''}) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnNotificationSetting = NativeModules.RnNotificationSetting
  ? NativeModules.RnNotificationSetting
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      },
    );

const notificationSettingsEmitter =
  Platform.OS === 'ios'
    ? new NativeEventEmitter(RnNotificationSetting)
    : DeviceEventEmitter;

const useNotificationSettingListner = (
  notificationSetting: FirebaseMessagingTypes.AuthorizationStatus,
) => {
  const [notificationAllowed, setNotificationAllowed] = useState<
    FirebaseMessagingTypes.AuthorizationStatus | undefined
  >(notificationSetting);

  useEffect(() => {
    const notiSetting = notificationSettingsEmitter.addListener(
      'NotificationSettingsChanged',
      (event: {status: FirebaseMessagingTypes.AuthorizationStatus}) => {
        setNotificationAllowed(event?.status ?? -1);
      },
    );
    return () => notiSetting.remove();
  }, []);
  return {notificationAllowed, setNotificationAllowed};
};

export default RnNotificationSetting;
type AuthorizationStatus = FirebaseMessagingTypes.AuthorizationStatus;
export {useNotificationSettingListner, type AuthorizationStatus};
