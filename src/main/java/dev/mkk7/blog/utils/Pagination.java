package dev.mkk7.blog.utils;

import lombok.Data;

@Data
public class Pagination {
	private int PAGE_SIZE = 10; 
	private int start;
	private int end;
	private int max;
	private int page;
}
