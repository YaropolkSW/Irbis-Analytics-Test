package com.irbis.irbisanalyticstest.dto;

import com.irbis.irbisanalyticstest.entity.NewsEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewsDTO {
    private String source;
    private String subject;
    private String news;

    public static NewsDTO of(final NewsEntity newsEntity) {
        return NewsDTO.builder()
            .source(newsEntity.getSource())
            .subject(newsEntity.getSubject())
            .news(newsEntity.getNews())
            .build();
    }
}
