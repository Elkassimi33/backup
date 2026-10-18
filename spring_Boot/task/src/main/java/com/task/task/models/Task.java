package com.task.task.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;
    private Boolean completed;
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	public boolean getCompleted() {
		
		return completed;
	}
	public String getTitle() {
		return title;
	}
	public Long getId() {
		return id;
	}
	
    
    
    
}
