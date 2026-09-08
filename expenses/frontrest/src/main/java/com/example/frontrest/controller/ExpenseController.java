package com.example.frontrest.controller;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.dto.ExpenseDto;
import com.example.expense.enums.ExpenseCategory;
import com.example.expense.mapper.ExpenseMapper;
import com.example.expense.model.Expense;
import com.example.expense.service.ExpenseService;
import com.example.frontrest.model.ExpenseModel;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

	private final ExpenseService expenseService;
	private final ExpenseMapper expenseMapper;
	
	public ExpenseController(ExpenseService expenseService, ExpenseMapper expenseMapper) {
		
		this.expenseService = expenseService;
		this.expenseMapper = expenseMapper;
		
	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllExpense(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction,
	        @RequestParam(required = false) String label,
	        @RequestParam(required = false) ExpenseCategory category) {

	    Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<Expense> pageResult = expenseService.findWithFilters(label, category, pageable);

	    List<ExpenseDto> content = expenseMapper.toDtoList(pageResult.getContent());
	            
	    BigDecimal totalAmount = content.stream().map(ExpenseDto::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("content", content);
	    response.put("page", pageResult.getNumber());
	    response.put("size", pageResult.getSize());
	    response.put("totalElements", pageResult.getTotalElements());
	    response.put("totalPages", pageResult.getTotalPages());
	    response.put("last", pageResult.isLast());
	    response.put("totalAmount",totalAmount);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id){
		
		return ResponseEntity.ok(expenseService.getExpenseById(id));
	}
	
	@PostMapping
	public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseModel body){
		
		ExpenseDto expense = expenseService.createExpense(body.label(), body.amount(), body.category(), body.date());
		return ResponseEntity.ok(expense);
		
	}
	@PutMapping("/{id}")
	public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseModel body){
		
		ExpenseDto expense = expenseService.updateExpense(id, body.label(), body.amount(), body.category(), body.date());
		return ResponseEntity.ok(expense);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExpense(@PathVariable Long id){
		
		expenseService.deleteExpense(id);
		return ResponseEntity.noContent().build();
	}
	
}
