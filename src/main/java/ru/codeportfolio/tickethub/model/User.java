package ru.codeportfolio.tickethub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("users")
@AllArgsConstructor
@Builder
public class User {
    @Id
    private Long id;
    private String name;

    @Builder.Default
    private Long balance = 0L;

    public void addBalance(Long rubles) {
        this.balance = balance + rubles;
    }
}
