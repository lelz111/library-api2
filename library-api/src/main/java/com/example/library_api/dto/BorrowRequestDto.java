package com.example.library_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRequestDto {

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("member_id")
    private Long memberId;
}


