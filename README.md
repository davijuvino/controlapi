> Projeto API Controle Despesas

> Endpoints
> /usuarios - usuarios associados ha uma despesa ou não associados
  ```json
      {
         "name":"João da Silva",
         "email":"joao.silva@example.com",
         "password":"senhaSegura123",
         "createAt":"2025-03-23T13:42:00",
         "deleteAt":null,
         "updateAt":null,
         "despesas":[
            {
               "name":"João da Silva",
               "category":"joao.silva@example.com",
               "description":"senhaSegura123",
               "createAt":"2025-03-23T13:42:00",
               "deleteAt":null,
               "updateAt":null
            },
            {
               "name":"João da Silva",
               "category":"joao.silva@example.com",
               "description":"senhaSegura123",
               "createAt":"2025-03-23T13:42:00",
               "deleteAt":null,
               "updateAt":null
            }
         ]
      }
  ````
      
>Referencias: 
      https://dco-unesp-bauru.github.io/tcc-bcc-2020-2/ThiagoHV/presentation-ThiagoHV.pdf
>Reference relaciation
When creating entity relationships in software development, especially with tools like **Spring Boot** and **JPA**, 
it’s essential to follow best practices to ensure clarity, maintainability, and performance. Here are some key guidelines:

### 1. **Design the Schema Carefully**
- Normalize your database to avoid redundancy and ensure consistency.
- Use meaningful relationships like `OneToOne`, `OneToMany`, `ManyToOne`, and `ManyToMany` appropriately.
- Clearly identify primary and foreign keys for all relationships.

### 2. **Bidirectional or Unidirectional?**
- Use **bidirectional relationships** only if you frequently need to navigate both ways. Otherwise, keep it **unidirectional** to reduce complexity.
- Always define the **`mappedBy`** attribute on the inverse side of a bidirectional relationship to prevent infinite loops in data processing.

### 3. **Lazy Loading by Default**
- Mark relationships as `LAZY` by default to avoid unnecessary data loading. Use `EAGER` loading cautiously and only when data is always needed.
- Example: `@OneToMany(fetch = FetchType.LAZY)`.

### 4. **Cascade Types**
- Use cascade operations wisely for child entities. For example:
      - `CascadeType.PERSIST` for saving child entities with parents.
      - `CascadeType.REMOVE` for deleting child entities when parents are deleted.
      - Avoid `CascadeType.ALL` unless you’re sure about its implications.

### 5. **Properly Handle Orphan Entities**
- Use `orphanRemoval = true` to automatically delete orphaned child records when no longer referenced by the parent.

### 6. **Avoid Circular JSON Issues**
- Use annotations like `@JsonIgnore` (for Jackson) or `@JsonBackReference` and `@JsonManagedReference` in bidirectional relationships to prevent infinite recursion during serialization.

### 7. **Indexes and Constraints**
- Define indexes and constraints (e.g., unique, not null) at the database level to ensure data integrity and improve performance.

### 8. **Leverage Lombok Wisely**
- While Lombok simplifies boilerplate code with `@Data`, consider excluding `toString`, `equals`, and `hashCode` generation for entities to avoid issues with circular references.
- Use specific annotations like `@Getter` and `@Setter` for more control.

### 9. **Use DTOs for External Communication**
- Avoid exposing entities directly in APIs. Use **Data Transfer Objects (DTOs)** to decouple your entity layer from the presentation layer.

### 10. **Maintain Clear Documentation**
- Document the purpose and behavior of relationships in code comments or architectural diagrams. This aids other developers in understanding the design.

By following these practices, you’ll create entities that are well-structured, scalable, and easy to work with in your Spring Boot projects. 🚀 Let me know if you'd like more details on any of these points!


### 11. **CrossOrigin**
- O que é @CrossOriginA anotação no Spring Boot oferece aos desenvolvedores uma maneira rápida e fácil de gerenciar solicitações de origem cruzada. Embora a política do CORS seja crucial para a segurança, os aplicativos modernos geralmente exigem algum nível de compartilhamento entre origens. Com o Spring Boot, os desenvolvedores podem equilibrar facilmente as necessidades de segurança com a funcionalidade.
- https://medium.com/@dev_RV/what-is-crossorigin-annotation-in-spring-boot-its-purpose-66125e1fc21a



### 12. **@Nullable** 
- Annotation to indicate that a specific parameter, return value, or field can be null . @NonNull : Annotation to indicate that a specific parameter, return value, or field cannot be null (not needed on parameters, return values, and fields where @NonNullApi and @NonNullFields apply, respectively).


### 13. **DTOs**
- DTOs (Data Transfer Objects) are simple objects that should not contain any business logic but only fields and getters/setters. They are used to transfer data between different layers of an application, such as between the controller and service layers.
- exemplo:
- https://www.javaguides.net/2022/12/spring-boot-dto-example-tutorial.html

### 14. **@CacheSpring**
- Spring Cache is a powerful abstraction that allows developers to cache data in a variety of ways, improving application performance and reducing the load on backend systems. It provides a consistent API for caching, regardless of the underlying caching technology used (e.g., Ehcache, Hazelcast, Redis, etc.). By using Spring Cache, developers can easily annotate methods to cache their results, manage cache entries, and configure cache settings without having to deal with the complexities of the underlying caching implementation.
- https://www.baeldung.com/spring-cache-tutorial
- https://www.baeldung.com/spring-cache
- https://www.baeldung.com/spring-cache-annotations
- https://www.baeldung.com/spring-cache-ehcache
- exemplo:  https://dev.to/noelopez/spring-cache-speed-up-your-app-1gf6
```java 
    @Cacheable("usuarios")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }




