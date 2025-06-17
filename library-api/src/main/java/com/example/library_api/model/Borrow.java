package com.example.library_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("borrow_date")
    private LocalDate borrowDate;
}
