package com.github.winter4666.bpofea.pet.domain.model;

import java.time.LocalDate;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.common.domain.validation.ValidatorHolder;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @NotNull
    private State state;

    @Builder
    public Pet(String name, LocalDate birthday, State state) {
        this.name = name;
        this.birthday = birthday;
        this.state = state;
    }

    public static class PetBuilder {
        public Pet build() {
            Pet pet = new Pet(name, birthday, state);
            ValidatorHolder.get().validateAndThrowExceptionIfNotValid(pet);
            if (birthday.isAfter(LocalDate.now())) {
                throw new DataInvalidException("Birthday should not be later than now");
            }
            return pet;
        }
    }

    public enum State {
        AVAILABLE, PENDING, SOLD
    }

}
