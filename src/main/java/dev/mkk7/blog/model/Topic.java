package dev.mkk7.blog.model;

import lombok.Data;

@Data
public class Topic {

	private Long count;
	private String key;
	
	
	public Topic(Long count, String key) {
		this.count = count;
		this.key = key;
	}
	
	
}
