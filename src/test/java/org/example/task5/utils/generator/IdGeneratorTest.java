package org.example.task5.utils.generator;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdGeneratorTest {

    @Mock
    Random random;

    @InjectMocks
    IdGenerator idGenerator;


    @ParameterizedTest
    @ValueSource(ints = {0, 100, 1000, 10000, 999, 324, 124, 100000})
    void generateId_needToGenerateId(int generatedNumber) {

        //Arrange
        when(random.nextInt(100000)).thenReturn(generatedNumber);

        //Act
        int result = idGenerator.generateId();

        //Assert
        assertEquals(generatedNumber, result);
    }
}