# react-native-notification-permission

## 安装

`$ npm install react-native-notification-permission --save`

_或者_

`$ yarn add react-native-notification-permission`

### react-native >= 0.60

`$ cd ios`

`$ pod install`

### react-native < 0.60

_自动_

`$ react-native link react-native-notification-permission`



## Usage
```javascript
import NotificationPermission from 'react-native-notification-permission';

NotificationPermission.hasPermission(); // 是否开启通知

NotificationPermission.openSystemNoticeView(); // 跳转到系统通知设置页面
```
