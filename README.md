# react-native-notification-permission

## 安装

`$ npm install react-native-notification-permission --save`

`或者`

`$ yarn add react-native-notification-permission`

`然后在项目根目录下运行`

`$ cd ios`

`$ pod install`


## Usage
```javascript
import NotificationPermission from 'react-native-notification-permission';

NotificationPermission.hasPermission(); // 是否开启通知，返回Promise<boolean>

NotificationPermission.openSystemNoticeView(); // 跳转到系统通知设置页面
```
