package com.skala.decase.domain.requirement.service.dto.response;

//TODO: ai쪽이랑 response 형식 맞추기
// description이 name인건가.?
public record UpdateRfpResponse(

        String status,
        String mod_reason,
        String id,  //"REQ-001",
        String type,  //"기능적"
        String description, //"UI/UX 디자인 가이드라인 개발",
        String detailed_description,  // "...",
        String acceptance_criteria,  //"모든 UI/UX 디자인이 일관된 스타일과 레이아웃을 따르도록 가이드라인이 마련되어야 한다.",
        String responsible_module,  //"UI/UX 모듈"
        String parent_id,  //"4.1.1"
        String source_pages,  //2,
        String category_large,  //"웹 기반 금융 정보 시스템",
        String category_medium,  // "사용자 인터페이스 화면 개발"
        String category_small,  //"UI/UX 디자인 가이드라인 개발"
        String difficulty,  //"중"
        String importance  //"중
) {
}