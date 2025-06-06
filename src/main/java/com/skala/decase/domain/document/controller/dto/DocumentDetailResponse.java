package com.skala.decase.domain.document.controller.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentDetailResponse {
    String docId;
    String name;
    LocalDateTime createdDate;
    String createdBy;
}
