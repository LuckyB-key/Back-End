package com.luckyb.domain.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Builder
@Setting(settingPath = "/elasticsearch/settings/settings.json", replicas = 0)
@Mapping(mappingPath = "/elasticsearch/mappings/mappings.json")
@Document(indexName = "shelter")
public class ShelterDocument {

  @Id
  private String id;
  private String name;
}