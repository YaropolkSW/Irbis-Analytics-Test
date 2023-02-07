package com.irbis.irbisanalyticstest.response;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GetAllSubjectsResponse {
    private final String subject;
}
