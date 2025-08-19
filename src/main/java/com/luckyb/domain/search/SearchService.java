package com.luckyb.domain.search;

import com.luckyb.domain.shelter.dto.ShelterDetailResponse;
import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

  private final ElasticsearchOperations elasticsearchOperations;
  private final ElasticRepository elasticRepository;
  private final ShelterRepository shelterRepository;

  public void save(Shelter shelter) {
    String cleanedName = StringUtils.trimAllWhitespace(shelter.getName());

    ShelterDocument doc = ShelterDocument.builder()
        .id(shelter.getShelterId())
        .name(cleanedName)
        .build();
    elasticRepository.save(doc);
  }

  public void delete(String shelterId) {
    elasticRepository.deleteById(shelterId);
  }

  public Page<ShelterDetailResponse> searchShelterByName(Pageable pageable, String shelterName) {

    Query query = QueryBuilders.match()
        .query(StringUtils.trimAllWhitespace(shelterName))
        .field("name")
        .build()
        ._toQuery();

    NativeQuery nativeQuery = new NativeQueryBuilder()
        .withQuery(query)
        .withPageable(pageable)
        .build();

    SearchHits<ShelterDocument> searchHits =
        elasticsearchOperations.search(nativeQuery, ShelterDocument.class);

    List<String> shelterIds = searchHits.get()
        .map(hit -> hit.getContent().getId())
        .collect(Collectors.toList());

    List<ShelterDetailResponse> shelters = shelterRepository.findAllById(shelterIds).stream()
        .map(ShelterDetailResponse::from)
        .collect(Collectors.toList());

    return new PageImpl<>(shelters, pageable, searchHits.getTotalHits());
  }
}
