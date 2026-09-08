package com.example.frontrest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.expense.dto.ExpenseDto;
import com.example.expense.enums.ExpenseCategory;
import com.example.expense.mapper.ExpenseMapper;
import com.example.expense.model.Expense;
import com.example.expense.service.ExpenseService;
import com.example.frontrest.controller.ExpenseController;
import com.example.frontrest.model.ExpenseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
	
	@MockitoBean
	private ExpenseService expenseService;
	
	@MockitoBean
	private ExpenseMapper expenseMapper;
	
	private Expense expense;
	private ExpenseDto expenseDto;
	private ExpenseModel expenseModel;
	
	@BeforeEach
	void setUp() {
		expense = new Expense();
		expense.setId(1L);
		expense.setLabel("Titre");
		expense.setAmount(BigDecimal.valueOf(100));
		expense.setCategory(ExpenseCategory.ALIMENTATION);
		expense.setDate(LocalDate.now());
		
		expenseDto = ExpenseDto.from(1L, "Titre",BigDecimal.valueOf(100), ExpenseCategory.ALIMENTATION, LocalDate.now(), null, null);
		
		expenseModel = new ExpenseModel("Titre", BigDecimal.valueOf(100), ExpenseCategory.ALIMENTATION, LocalDate.now());
	}
	@Test
	@DisplayName("Get /api/expenses - success")
	void getAllExpenses_success() throws Exception {
		
		Page<Expense> page = new PageImpl<Expense>(List.of(expense));
		when(expenseService.findWithFilters(eq("Titre"),eq(ExpenseCategory.ALIMENTATION), any(Pageable.class))).thenReturn(page);
		when(expenseMapper.toDtoList(any())).thenReturn(List.of(expenseDto));
		
		mockMvc.perform(get("/api/expenses")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("label", "Titre")
                .param("category", ExpenseCategory.ALIMENTATION.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(expenseDto.id()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true));
		
		verify(expenseService).findWithFilters(eq("Titre"),eq(ExpenseCategory.ALIMENTATION), any(Pageable.class));
	}
	
	@Test
	@DisplayName("Get /api/expenses/{id} - success")
	void getExpenseById_success() throws Exception {
		Long expenseId = 1L;
		when(expenseService.getExpenseById(expenseId)).thenReturn(expenseDto);
		
		mockMvc.perform(get("/api/expenses/{id}", expenseId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(expenseDto.id()))
			.andExpect(jsonPath("$.label").value(expenseDto.label()));
		
		verify(expenseService).getExpenseById(expenseId);
	}
	
	@Test
	@DisplayName("Post /api/expenses - success")
	void createExpense_success() throws Exception {
		
		when(expenseService.createExpense(expenseModel.label(), expenseModel.amount(), expenseModel.category(), expenseModel.date())).thenReturn(expenseDto);
		mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expenseDto.id()))
                .andExpect(jsonPath("$.label").value(expenseDto.label()));

        verify(expenseService).createExpense(expenseModel.label(), expenseModel.amount(), expenseModel.category(), expenseModel.date());
		
	}
	@Test
	@DisplayName("Put /api/expenses/{id} - success")
	void updateExpense_success() throws Exception {
		
		Long expenseId = 1L;
        when(expenseService.updateExpense(expenseId, expenseModel.label(), expenseModel.amount(), expenseModel.category(), expenseModel.date())).thenReturn(expenseDto);

        mockMvc.perform(put("/api/expenses/{id}", expenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expenseModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expenseDto.id()))
                .andExpect(jsonPath("$.label").value(expenseDto.label()));

        verify(expenseService).updateExpense(expenseId, expenseModel.label(), expenseModel.amount(), expenseModel.category(), expenseModel.date());
	}
	
	@Test
	@DisplayName("Delete /api/expenses/{id} - success")
	void deleteExpenses_success() throws Exception{
		
		Long expenseId = 1L;
		doNothing().when(expenseService).deleteExpense(expenseId);
		
		mockMvc.perform(delete("/api/expenses/{id}",expenseId)).andExpect(status().isNoContent());
		
		verify(expenseService).deleteExpense(expenseId);
	
	}
}
