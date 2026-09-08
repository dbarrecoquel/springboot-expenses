package com.example.expense.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.expense.enums.ExpenseCategory;
import com.example.expense.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	@Query("""
            SELECT e FROM Expense e
            WHERE (:label IS NULL OR LOWER(e.label) LIKE LOWER(CONCAT('%', CAST(:label AS string), '%')))
              AND (:category IS NULL OR e.category = :category)
        """)
    Page<Expense> findWithFilters(
        @Param("label") String label,
        @Param("category") ExpenseCategory category,
        Pageable pageable
    );
}
