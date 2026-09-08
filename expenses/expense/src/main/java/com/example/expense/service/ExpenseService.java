package com.example.expense.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.expense.dto.ExpenseDto;
import com.example.expense.enums.ExpenseCategory;
import com.example.expense.mapper.ExpenseMapper;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final ExpenseMapper expenseMapper;
	
	public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
		this.expenseRepository = expenseRepository;
		this.expenseMapper = expenseMapper;
	}
	
	public Page<Expense> getAllExpense(Pageable pageable) {
		return expenseRepository.findAll(pageable);
	}
	
	public Page<Expense> findWithFilters(String label, ExpenseCategory category, Pageable pageable) {
		return expenseRepository.findWithFilters(label, category, pageable);
	}
	
	public List<ExpenseDto> getAllExpense() {
		return expenseMapper.toDtoList(expenseRepository.findAll());
	}
	
	public ExpenseDto getExpenseById(Long id) {
		Expense expense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense introuvable"));
		
		return expenseMapper.toDto(expense);
	}
	
	public ExpenseDto createExpense(String label, BigDecimal amount, ExpenseCategory category, LocalDateTime date) {
		
		Expense expense = new Expense();
		expense.setLabel(label);
		expense.setAmount(amount);
		expense.setCategory(category);
		expense.setDate(date);
		expense.setCreatedAt(LocalDateTime.now());
		expense.setUpdatedAt(LocalDateTime.now());
		
		Expense saved = expenseRepository.save(expense);
		
		return expenseMapper.toDto(saved);
		
	}
	public ExpenseDto updateExpense(Long id, String label, BigDecimal amount, ExpenseCategory category, LocalDateTime date) {
		
		Expense expense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense introuvable"));
		
		expense.setLabel(label);
		expense.setAmount(amount);
		expense.setCategory(category);
		expense.setDate(date);
		expense.setUpdatedAt(LocalDateTime.now());
		
		Expense saved = expenseRepository.save(expense);
		
		return expenseMapper.toDto(saved);
		
	}
	
	public void deleteExpense(Long id) {
		expenseRepository.deleteById(id);
	}
}
