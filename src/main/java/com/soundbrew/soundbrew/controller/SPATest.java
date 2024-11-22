package com.soundbrew.soundbrew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class SPATest {

    @GetMapping("/me")
    public User getUser() {
        // 임의의 사용자 데이터 반환
        return new User(1L, "John Doe", "john.doe@example.com");
    }

    // User 클래스
    public static class User {
        private Long id;
        private String name;
        private String email;

        // 생성자, Getter, Setter
        public User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
