package com.example.expense.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.expense.dto.ExpenseDto;
import com.example.expense.model.Expense;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

	ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);
	
	ExpenseDto toDto(Expense expense);
	
	Expense toEntity(ExpenseDto dto);
	
	List<ExpenseDto> toDtoList(List<Expense> expenses);
	
	List<Expense> toEntityList(List<ExpenseDto> dtos);
}
