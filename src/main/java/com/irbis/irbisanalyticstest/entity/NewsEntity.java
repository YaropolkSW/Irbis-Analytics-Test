package com.irbis.irbisanalyticstest.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "news_table")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "source")
    private String source;

    @Column(name = "subject")
    private String subject;

    @Column(name = "news")
    private String news;
}
