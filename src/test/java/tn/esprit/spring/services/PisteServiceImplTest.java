package tn.esprit.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PisteServiceImplTest {

    @InjectMocks
    private PisteServicesImpl pisteService;  // This is your service implementation

    @Mock
    private IPisteRepository pisteRepository;  // Mock the repository

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testRetrieveAllPistes() {
        // Arrange
        Piste piste1 = new Piste(1L, "Blue Trail", Color.BLUE, 1000, 30, null);
        Piste piste2 = new Piste(2L, "Red Trail", Color.RED, 1200, 40, null);
        List<Piste> pistes = Arrays.asList(piste1, piste2);

        when(pisteRepository.findAll()).thenReturn(pistes);

        // Act
        List<Piste> result = pisteService.retrieveAllPistes();

        // Assert
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        // Arrange
        Piste piste = new Piste(1L, "Green Trail", Color.GREEN, 800, 20, null);
        when(pisteRepository.save(piste)).thenReturn(piste);

        // Act
        Piste result = pisteService.addPiste(piste);

        // Assert
        assertNotNull(result);
        assertEquals("Green Trail", result.getNamePiste());
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRetrievePiste() {
        // Arrange
        Piste piste = new Piste(1L, "Black Trail", Color.BLACK, 1500, 50, null);
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        // Act
        Piste result = pisteService.retrievePiste(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Black Trail", result.getNamePiste());
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void testRemovePiste() {
        // Act
        pisteService.removePiste(1L);

        // Assert
        verify(pisteRepository, times(1)).deleteById(1L);
    }
}
