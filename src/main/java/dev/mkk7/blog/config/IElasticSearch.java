package dev.mkk7.blog.config;

import java.security.KeyStoreException;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

public interface IElasticSearch {
	ElasticsearchClient client() throws KeyStoreException, Exception;
}
