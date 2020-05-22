import { NativeModules, Platform, Linking } from 'react-native';

const { NotificationPermission } = NativeModules;

if (Platform.OS === "ios") {
    NotificationPermission.openSystemNoticeView = function() {
        Linking.openURL('app-settings:')
            .catch(err => console.log('openSystemSetting error', err));
    }
}

export default NotificationPermission;
