package com.irbis.irbisanalyticstest.response;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GetAllSourcesResponse {
    private final String source;
}
