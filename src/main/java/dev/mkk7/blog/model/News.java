package dev.mkk7.blog.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "news3")
public class News {
	
	@Id
	private String doc_id;
	private String doc_title;
	private String doc_source;
	private Date doc_published;
	private Date created;
	private String context;
	private String doc_class_class;
	private String doc_class_code;
	
	public String getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
	public String getDoc_title() {
		return doc_title;
	}
	public void setDoc_title(String doc_title) {
		this.doc_title = doc_title;
	}
	public String getDoc_source() {
		return doc_source;
	}
	public void setDoc_source(String doc_source) {
		this.doc_source = doc_source;
	}
	public Date getDoc_published() {
		return doc_published;
	}
	public void setDoc_published(Date doc_published) {
		this.doc_published = doc_published;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getDoc_class_class() {
		return doc_class_class;
	}
	public void setDoc_class_class(String doc_class_class) {
		this.doc_class_class = doc_class_class;
	}
	public String getDoc_class_code() {
		return doc_class_code;
	}
	public void setDoc_class_code(String doc_class_code) {
		this.doc_class_code = doc_class_code;
	}
	@Override
	public String toString() {
		return "News [doc_id=" + doc_id + ", doc_title=" + doc_title + ", doc_source=" + doc_source + ", doc_published="
				+ doc_published + ", created=" + created + ", context=" + context + ", doc_class_class="
				+ doc_class_class + ", doc_class_code=" + doc_class_code + "]";
	}
	
	public String getFormatPublished() {
		return new SimpleDateFormat("yyyy-MM-dd").format(doc_published);
	}
	
	
}
