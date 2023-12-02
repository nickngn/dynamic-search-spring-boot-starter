# Spring Boot Starter Dynamic Search

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![javadoc](https://javadoc.io/badge2/io.github.nickngn/spring-boot-starter-dynamic-search/javadoc.svg)](https://javadoc.io/doc/io.github.nickngn/spring-boot-starter-dynamic-search)

## What is this?

This is the fast-food set up for Dynamic Search (aka. Advanced Search) for Spring Boot, built on top of Spring Data Jpa.

This provides typical settings for Criteria Search / Specification usages & validation supports.

Instead of boilerplate codes, what you need are:

Controller:

```java title=SearchController.java
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<Page<SearchEntity>> search(@RequestBody SearchCriteria criteria) {
        return ResponseEntity.ok(searchService.search(criteria));
    }
}
```

Service:

```java title=SearchService.java
@Service
@RequiredArgsConstructor
public class SearchService {

    private final EntityRepository entityRepository;
    private final SpecBuilder<SearchEntity> builder = SpecBuilders.getInstance(SearchEntity.class);

    public Page<SearchEntity> search(SearchCriteria searchCriteria) {
        Specification<SearchEntity> spec = builder.build(searchCriteria.getCriteria());
        return entityRepository.findAll(spec, searchCriteria.getPageable());
    }
}
```

Repository:

```java
@Repository
public interface EntityRepository extends JpaRepository<SearchEntity, Long>,
        JpaSpecificationExecutor<SearchEntity> {
}
```

That's it! Everything is on your table.

### Installation:

Maven:

```xml
<dependency>
    <groupId>io.github.nickngn</groupId>
    <artifactId>spring-boot-starter-dynamic-search</artifactId>
    <version>0.1.0</version>
</dependency>
```

Gradle:

```groovy
implementation group: 'io.github.nickngn', name: 'spring-boot-starter-dynamic-search', version: '0.1.0'
```

### Validate syntax for key/values using Jakarta/Javax annotations



- Generate SQL conditions in WHERE clause.
