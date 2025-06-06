package com.skala.decase.domain.document.mapper;

import com.skala.decase.domain.document.controller.dto.DocumentDetailResponse;
import com.skala.decase.domain.document.domain.Document;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocumentMapper {

    public DocumentDetailResponse toDetailResponse(Document document) {
        return DocumentDetailResponse.builder()
                .docId(document.getDocId())
                .name(document.getName())
                .createdDate(document.getCreatedDate())
                .createdBy(document.getCreatedBy().getName())
                .build();
    }


}