package com.heptagon.thirema.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "telegram_username", unique = true, nullable = false)
    private String telegramUsername;

    @Column(name = "telegram_user_id", unique = true)
    private Integer telegramUserId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "vat_number")
    private String vatNumber;
}
