package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        final List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        final MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    //@GetMapping("/v1/users/{id}")     // URI를 이용한 버전관리
    //@GetMapping(value = "/users/{id}/", params = "version=1")    // 파라미터를 이용한 버전관리
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")   // header값을 이용한 버전 관리
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")  // MIME TYPE을 이용한 버전관리
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        final User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");   // 필터에서 제외할 필드명

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);  // UserInfo는 User클래스에서 정해놓은 jsonFilter이름

        final MappingJacksonValue mapping = new MappingJacksonValue(user);  // 조회한 user값을 json으로 매핑하고
        mapping.setFilters(filters);                                        // 필터링된걸 세팅해준다.

        return mapping; // 필터링된 값을 반환하기 위해서는 그냥 User같은 도메인객체를 반환할수 없어서 매핑된 값을 반환한다.
    }

    //@GetMapping("/v2/users/{id}")
    //@GetMapping(value = "/users/{id}/", params = "version=2")
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        final User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");   // 필터에서 제외할 필드명

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);  // UserInfo는 User클래스에서 정해놓은 jsonFilter이름

        final MappingJacksonValue mapping = new MappingJacksonValue(userV2);  // 조회한 user값을 json으로 매핑하고
        mapping.setFilters(filters);                                        // 필터링된걸 세팅해준다.

        return mapping; // 필터링된 값을 반환하기 위해서는 그냥 User같은 도메인객체를 반환할수 없어서 매핑된 값을 반환한다.
    }
}
