package com.luckyb.domain.search;

import com.luckyb.domain.shelter.dto.ShelterDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/search")
@RestController
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/shelters")
  public ResponseEntity<Page<ShelterDetailResponse>> searchShelter(
      @PageableDefault Pageable pageable,
      @RequestParam String name
  ) {
    return ResponseEntity.ok(searchService.searchShelterByName(pageable, name));
  }
}
