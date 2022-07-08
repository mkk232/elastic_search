package dev.mkk7.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import dev.mkk7.blog.config.IElasticSearch;
import dev.mkk7.blog.model.News;
import dev.mkk7.blog.model.Topic;
import dev.mkk7.blog.service.SearchService;
import dev.mkk7.blog.utils.Pagination;


@RestController
public class SearchController {
	
	@Autowired
	private IElasticSearch elasticSearch;
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("search");
		return mv; 
	}

	@PostMapping(value = "/search")
	public HashMap<String, Object> test(
						 	@RequestParam(required = false, defaultValue = "1") int page,
							@RequestParam(required = false, defaultValue = "") String keyword,
							@RequestParam(required = false) List<String> subclassArr,
							@RequestParam(required = false) List<String> clasificarArr) throws Exception {
		
		HashMap<String, Object> map = new HashMap<>();
		ElasticsearchClient client = elasticSearch.client();
		ArrayList<News> news = new ArrayList<News>();
		
		ModelAndView mv = new ModelAndView();
		Pagination pagination = new Pagination();
		
		pagination.setPage(page);
		pagination.setStart(((pagination.getPage() - 1) / pagination.getPAGE_SIZE()) * pagination.getPAGE_SIZE() + 1 );
		pagination.setEnd(pagination.getStart() + pagination.getPAGE_SIZE() - 1);

		//FieldValue list = FieldValue.of(t -> t.nullValue(checkArr));
		//List<String> b = Arrays.asList(checkArr);
		
		//List<FieldValue> a = b.stream().map().collect(Collectors.toList());
		
		SearchResponse<News> search = this.searchService.list(pagination, keyword, client, subclassArr, clasificarArr);
		
		List<News> collect = search.hits().hits().stream().map(s-> s.source()).collect(Collectors.toList());
		
		List<Map<String, List<String>>> highlightContent = search.hits().hits().stream()
                .map(news3Hit -> news3Hit.highlight()).collect(Collectors.toList());
		
		List<Topic> code = search.aggregations().get("code").sterms().buckets().array().stream()
		.map(s -> new Topic(s.docCount(), s.key())).collect(Collectors.toList());
		
		List<Topic> cla = search.aggregations().get("class").sterms().buckets().array().stream()
				.map(s -> new Topic(s.docCount(), s.key())).collect(Collectors.toList());
		
		
		map.put("code", code);
		map.put("cla", cla);
//		search.aggregations().get("topic").
		map.put("collect", collect);
		map.put("highlight", highlightContent);
		//map.put("aggs", topic);
		
		pagination.setMax((int)search.hits().total().value() / pagination.getPAGE_SIZE());
		if((int)search.hits().total().value() % pagination.getPAGE_SIZE() > 0) {
			pagination.setMax((int)search.hits().total().value() / pagination.getPAGE_SIZE() + 1);
		}
		if(pagination.getEnd() > pagination.getMax()) {
			pagination.setEnd(pagination.getMax());
		}
		
		map.put("page", pagination);
		return map;
	}

	
	
}
