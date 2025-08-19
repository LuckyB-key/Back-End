package com.luckyb.domain.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticRepository extends ElasticsearchRepository<ShelterDocument, String> {

}
