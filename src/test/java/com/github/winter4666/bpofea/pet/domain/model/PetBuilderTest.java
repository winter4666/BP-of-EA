package com.github.winter4666.bpofea.pet.domain.model;

import java.util.stream.Stream;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.pet.datafaker.PetBuilder;
import jakarta.validation.ConstraintViolationException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PetBuilderTest {

    @ParameterizedTest
    @MethodSource("petBuilderProvider")
    void should_throw_exception_when_create_pet_given_invalid_pet(Pet.PetBuilder petBuilder) {
        Exception exception = assertThrows(Exception.class, petBuilder::build);

        assertThat(exception, anyOf(instanceOf(ConstraintViolationException.class), instanceOf(DataInvalidException.class)));
    }

    static Stream<Pet.PetBuilder> petBuilderProvider() {
        return Stream.of(
                new PetBuilder().name(" ").createPetBuilder(),
                new PetBuilder().birthday(null).createPetBuilder(),
                new PetBuilder().state(null).createPetBuilder()
        );
    }

}