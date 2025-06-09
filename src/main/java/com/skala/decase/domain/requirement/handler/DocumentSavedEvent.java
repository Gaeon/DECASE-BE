package com.skala.decase.domain.requirement.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentSavedEvent {

    //파일 메모리에 임시 저장
    private final byte[] fileContent;      // MultipartFile 대신 바이트 배열
    private final String originalFilename; // 원본 파일명
    private final String contentType;      // 파일 타입

    private final Long projectId;
    private final Long memberId;
    private final String documentId;
}
