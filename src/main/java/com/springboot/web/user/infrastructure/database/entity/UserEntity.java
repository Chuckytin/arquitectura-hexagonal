package com.springboot.web.user.infrastructure.database.entity;

import com.springboot.web.user.domain.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // Los 4 campos booleanos de Spring Security los gestiona UserDetails directamente.
    // No hace falta persistirlos aquí salvo necesidad del control por usuario en BBDD.
//    @Builder.Default
//    @Column(name = "account_non_expired")
//    private boolean accountNonExpired = true;
//
//    @Builder.Default
//    @Column(name = "account_non_locked")
//    private boolean accountNonLocked = true;
//
//    @Builder.Default
//    @Column(name = "credentials_non_expired")
//    private boolean credentialsNonExpired = true;
//
//    @Builder.Default
//    @Column(name = "enabled")
//    private boolean enabled = true;

}
