package com.github.winter4666.bpofea.pet.datafaker;

import java.time.LocalDate;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.pet.domain.model.Pet;

public class PetBuilder {

    private static final Faker FAKER = new Faker();

    private Long id;

    private String name = FAKER.educator().course();

    private LocalDate birthday = LocalDate.of(2023, 1, 1);

    private Pet.State state = Pet.State.AVAILABLE;

    public PetBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PetBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PetBuilder birthday(LocalDate birthday)  {
        this.birthday = birthday;
        return this;
    }

    public PetBuilder state(Pet.State state) {
        this.state = state;
        return this;
    }

    public Pet build() {
        return createPetBuilder().build();
    }

    public Pet.PetBuilder createPetBuilder() {
        return Pet.builder()
                .name(name)
                .birthday(birthday)
                .state(state);
    }

}
