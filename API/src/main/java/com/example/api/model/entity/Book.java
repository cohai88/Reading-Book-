package com.example.api.model.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private Integer publishYear;
    @Column(nullable = false, length = 1000)
    private String image;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    private boolean deleted;
}
