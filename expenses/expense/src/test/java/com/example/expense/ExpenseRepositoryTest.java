package com.example.expense;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.expense.enums.ExpenseCategory;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = TestApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/clean-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("ExpenseRepository — Integration Tests")
public class ExpenseRepositoryTest {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
	    .withDatabaseName("expense_test")
	    .withUsername("testuser")
	    .withPassword("testpass")
	    .withReuse(true);
	
	@DynamicPropertySource
	static void overideDataSource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.datasource.hikari.auto-commit", () -> "false");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
	}
	
	@Autowired ExpenseRepository expenseRepository;
	@Autowired TestEntityManager em;
	
	private Expense expense;
	
	@BeforeEach
	void setUp() {
		
		expense = new Expense();
		expense.setLabel("Titre");
		expense.setAmount(BigDecimal.valueOf(100));
		expense.setCategory(ExpenseCategory.ALIMENTATION);
		expense.setDate(LocalDate.now());
		
		em.persistAndGetId(expense);
		em.clear();
	}
	
	@Nested
	@DisplayName("Recherches simples")
	class FindTests {
		
		@Test
		@DisplayName("findWithFilters_found")
		void findWithFilters_found() {
			
			Pageable pageable = PageRequest.of(0, 10);
			
			Page<Expense> expenses = expenseRepository.findWithFilters("Titre", null, pageable);
			
			assertThat(expenses).isNotNull();
			assertThat(expenses.getContent()).isNotEmpty();
			assertThat(expenses.getContent()).hasSize(1);
		}
		
		@Test
		@DisplayName("findWithFilters_notfound")
		void findWithFilters_notfound() {
			
			Pageable pageable = PageRequest.of(0, 10);
			
			Page<Expense> expenses = expenseRepository.findWithFilters("Titre2", null, pageable);
			
			assertThat(expenses.getContent()).isEmpty();
		}
	}
}
