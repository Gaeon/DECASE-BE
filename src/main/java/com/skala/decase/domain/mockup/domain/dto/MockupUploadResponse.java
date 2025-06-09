package com.skala.decase.domain.mockup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockupUploadResponse {
	private String filename;
	private String url; // 파일 접근용 URL이 있다면
}