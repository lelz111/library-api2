package com.example.library_api.repository;

import com.example.library_api.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByMemberId(Long memberId);
}
