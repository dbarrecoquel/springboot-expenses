package com.example.expense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.expense.dto.ExpenseDto;
import com.example.expense.enums.ExpenseCategory;
import com.example.expense.mapper.ExpenseMapper;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.service.ExpenseService;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

	@Mock
	private ExpenseRepository expenseRepository;
	
	@Mock
	private ExpenseMapper expenseMapper;
	
	@InjectMocks
	private ExpenseService expenseService;
	
	private Expense expense;
	private ExpenseDto expenseDto;
	
	@BeforeEach
	void setUp() {
		
		expense = new Expense();
		expense.setId(1L);
		expense.setLabel("Titre");
		expense.setAmount(BigDecimal.valueOf(100));
		expense.setCategory(ExpenseCategory.ALIMENTATION);
		expense.setDate(LocalDate.now());
		
		expenseDto = ExpenseDto.from(1L, "Titre",BigDecimal.valueOf(100), ExpenseCategory.ALIMENTATION, LocalDate.now(), null, null);
		
	}
	
	@Nested
	@DisplayName("Recherches simples")
	class FindTests {
		
		@Test
		@DisplayName("shouldGetAllExpense pageable")
		void shouldGetAllExpensePageable() {
			
			Pageable pageable = PageRequest.of(0, 10);
			Page<Expense> page = new PageImpl<Expense>(List.of(expense));
			
			when(expenseRepository.findAll(pageable)).thenReturn(page);
			
			Page<Expense> result = expenseService.getAllExpense(pageable);
			
			assertThat(result.getContent()).hasSize(1);
			verify(expenseRepository, times(1)).findAll(pageable);
		}
		@Test
		@DisplayName("shouldGetAllExpense list")
		void shouldGetAllExpenseList() {
			
			List<Expense> expenses = List.of(expense);
			List<ExpenseDto> expensesDto = List.of(expenseDto);
			
			when(expenseRepository.findAll()).thenReturn(expenses);
			when(expenseMapper.toDtoList(expenses)).thenReturn(expensesDto);
			
			List<ExpenseDto> result = expenseService.getAllExpense();
			
			assertThat(result).hasSize(1);
			verify(expenseRepository, times(1)).findAll();
		}
		
		@Test
		@DisplayName("shouldGetExpenseById")
		void shouldGetExpenseById() {
			
			when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
			when(expenseMapper.toDto(expense)).thenReturn(expenseDto);
			
			ExpenseDto result = expenseService.getExpenseById(1L);
			
			assertThat(result).isNotNull();
			assertThat(result.label()).isEqualTo("Titre");
			
			verify(expenseRepository, times(1)).findById(1L);
		}
		
		@Test
		@DisplayName("shouldFindWithFilters")
		void shouldFindWithFilters() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Expense> page = new PageImpl<Expense>(List.of(expense));
			
			when(expenseRepository.findWithFilters("Titre", null, pageable)).thenReturn(page);
			
			Page<Expense> result = expenseService.findWithFilters("Titre", null, pageable);
			
			assertThat(result.getContent()).hasSize(1);
			verify(expenseRepository, times(1)).findWithFilters("Titre", null, pageable);
			
		}
	}
	
	@Nested
	@DisplayName("Save test")
	class SaveTests {
		
		@Test
		@DisplayName("shouldCreateExpense")
		void shouldCreateExpense() {
			
			when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
			when(expenseMapper.toDto(expense)).thenReturn(expenseDto);
			
			ExpenseDto result = expenseService.createExpense("Titre", BigDecimal.valueOf(100), ExpenseCategory.ALIMENTATION, LocalDate.now());
		    
			assertThat(result).isEqualTo(expenseDto);
			
			verify(expenseRepository).save(any(Expense.class));
		}
		
		@Test
		@DisplayName("shouldUpdateExpense")
		void shouldUpdateExpense() {
			
			when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
			when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
			when(expenseMapper.toDto(expense)).thenReturn(expenseDto);
			
			ExpenseDto result = expenseService.updateExpense(1L,"Titre", BigDecimal.valueOf(100), ExpenseCategory.ALIMENTATION, LocalDate.now());
		    
			assertThat(result).isEqualTo(expenseDto);
			
			verify(expenseRepository).save(any(Expense.class));
		}
	}
	
	@Nested
	@DisplayName("Remove tests")
	class RemoveTests {
		
		@Test
		@DisplayName("shouldRemoveExpense")
		void shouldRemoveExpense() {
			expenseService.deleteExpense(1L);
			verify(expenseRepository,times(1)).deleteById(1L);
		}
	}
}
