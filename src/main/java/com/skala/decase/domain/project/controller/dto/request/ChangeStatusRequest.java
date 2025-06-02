package com.skala.decase.domain.project.controller.dto.request;

import com.skala.decase.domain.project.domain.Permission;

public record ChangeStatusRequest(
        Permission permission
) {
}
