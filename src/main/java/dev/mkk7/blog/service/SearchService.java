package dev.mkk7.blog.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.RestClientBuilder;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlighterOrder;
import dev.mkk7.blog.model.News;
import dev.mkk7.blog.utils.Pagination;

@Service
public class SearchService {


	public SearchResponse<News> list(Pagination pagination, String keyword, ElasticsearchClient client,
			List<String> subclassArr, List<String> clasificarArr) throws Exception {
		
		List<Query> queryList = new ArrayList<Query>();
		List<FieldValue> fieldList = new ArrayList<FieldValue>();
		
		if(subclassArr != null) {
			for(String check : subclassArr) {
				fieldList.add(new FieldValue.Builder().stringValue(check).build());
			}
			queryList.add(Query.of(q -> q
					.terms(t -> t
							.field("doc_class_code")
							.terms(te -> te.value(fieldList)))));
		}
		
		if(clasificarArr != null) {
			for(String check : clasificarArr) {
				fieldList.add(new FieldValue.Builder().stringValue(check).build());
			}
			queryList.add(Query.of(q -> q
					.terms(t -> t
							.field("doc_class_class")
							.terms(te -> te.value(fieldList)))));
		}

		SearchResponse<News> search = null;
			search = client.search(s -> s
				    .index("news3")
				    .sort(sort -> sort
				    		.field(f -> f
				    				.field("_score").order(SortOrder.Desc)))
				    .size(pagination.getPAGE_SIZE())
				    .from((pagination.getPage() * pagination.getPAGE_SIZE()) - (pagination.getPAGE_SIZE()))
				    .query(q -> q
				    		.bool(b -> b
				    				.must(m -> m
				    						.multiMatch(mu -> mu
				    								.type(TextQueryType.Phrase)
				    								.query(keyword)
				    								.fields("doc_title", "context", "doc_source")))
					    			.filter(queryList)))
				    .aggregations("code", a -> a				    		
				    		.terms(t -> t
				    				.field("doc_class_code").size(100)))
				    .aggregations("class", c -> c
				    		.terms(t -> t
				    				.field("doc_class_class").size(10)))
				    .highlight(h -> h
				    		.fields("*", f -> f
				    				.preTags(List.of("<em class='highlight'>"))
				    				.postTags(List.of("</em>"))
				    				.fragmentSize(500)
				    				.numberOfFragments(2)
				    		.order(HighlighterOrder.Score))),
				    News.class);
		
		return search;
	}

}
