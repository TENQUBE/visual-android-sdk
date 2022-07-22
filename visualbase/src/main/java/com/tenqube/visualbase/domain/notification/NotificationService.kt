package com.tenqube.visualbase.domain.notification

import com.tenqube.visualbase.domain.notification.dto.NotificationDto

interface NotificationService {
    fun show(command: NotificationDto)
}