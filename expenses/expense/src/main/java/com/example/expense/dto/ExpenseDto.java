package com.example.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.expense.enums.ExpenseCategory;

public record ExpenseDto(Long id,
						String label,
						BigDecimal amount,
						ExpenseCategory category,
						LocalDateTime date,
						LocalDateTime createdAt,
						LocalDateTime updatedAt) {
	
	public static ExpenseDto from(Long id,
						String label,
						BigDecimal amount,
						ExpenseCategory category,
						LocalDateTime date,
						LocalDateTime createdAt,
						LocalDateTime updatedAt) {
		
		return new ExpenseDto(id, label, amount, category, date, createdAt, updatedAt);
	}

}
