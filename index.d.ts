declare module NotificationPermission {
    function hasPermission(): Promise<string>;
    function openSystemNoticeView(): void;
}

export default NotificationPermission;
