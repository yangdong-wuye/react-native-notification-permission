declare module NotificationPermission {
    function hasPermission(): Promise<boolean>;
    function openSystemNoticeView(): void;
}

export default NotificationPermission;
