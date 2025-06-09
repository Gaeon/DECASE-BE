package com.skala.decase.domain.requirement.service.dto.response;

//TODO: ai쪽이랑 response 형식 맞추기
// description이 name인건가.?
public record UpdateRfpResponse(

        String status,
        String mod_reason,
        String id,  //"REQ-001",
        String type,  //"기능"
        String name, //"UI/UX 디자인 가이드라인 개발",
        String description_content,  
        String target_task,  
        String processing_detail,  
        String source_pages,  //2,
        String category_large,  //"웹 기반 금융 정보 시스템",
        String category_medium,  // "사용자 인터페이스 화면 개발"
        String category_small,  //"UI/UX 디자인 가이드라인 개발"
        String difficulty,  //"중"
        String importance,  //"중
        String acceptance_criteria
) {
}