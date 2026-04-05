package com.bootcamp4u.entity;

import com.bootcamp4u.common.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
// 1. Override the DELETE command
@SQLDelete(sql = "UPDATE users SET is_active = false WHERE id = ? AND version = ?")
// 2. Filter all SELECT commands
@SQLRestriction("is_active = true")
@Getter                 // Generates all getters
@Setter                 // Generates all setters
@NoArgsConstructor      // Generates default constructor
@AllArgsConstructor     // Generates constructor with all fields
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @NotBlank(message ="Firstname is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name="username", unique = true, nullable = false, updatable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name="password", nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name="email", nullable = false, unique = true, updatable = false)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(name="phone", nullable = false)
    private String phone;

    @NotNull(message = "Role is required")
    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return isActive; }

    @Override
    public boolean isAccountNonLocked() { return isActive; }

    @Override
    public boolean isCredentialsNonExpired() { return isActive; }

    @Override
    public boolean isEnabled() { return isActive; }
}