package com.example.frontrest.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.expense.enums.ExpenseCategory;

public record ExpenseModel(String label,
		 				   BigDecimal amount,
		 				   ExpenseCategory category,
		 				   LocalDate date) {
	

}
