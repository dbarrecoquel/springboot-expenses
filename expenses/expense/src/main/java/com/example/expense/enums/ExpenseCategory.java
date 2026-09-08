package com.example.expense.enums;

public enum ExpenseCategory {

	ALIMENTATION("Alimentation"),
	TRANSPORT("Transport"),
	LOGEMENT("Logement"),
	LOISIRS("Loisirs"),
	SANTE("Sante"),
	AUTRE("Autre");
	
	private String label;
	
    ExpenseCategory(String label) {
		this.label = label;
	}
    
    public String getLabel() {
    	return this.label;
    }
}
