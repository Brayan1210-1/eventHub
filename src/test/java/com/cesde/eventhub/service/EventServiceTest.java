package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.EventStatus;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.mapper.EventMapper;
import com.cesde.eventhub.repository.EventRepository;
import com.cesde.eventhub.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private UserService userService;

    @Spy
    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    @InjectMocks
    private EventService eventService;

   
    private Event eventEntity;
    private User organizer;

    @BeforeEach
    void setUp() {
        organizer = new User();
        organizer.setId(UUID.randomUUID());
        organizer.setEmail("organizador@eventhub.com");

       
        eventEntity = new Event();
        eventEntity.setId(500L);
        eventEntity.setName("Concierto de Rock");
        eventEntity.setOrganizer(organizer);
        eventEntity.setStatus(EventStatus.BORRADOR);
    }


    @Test
    void findEventById_ShouldReturnEvent_WhenIdExists() {
      
        when(eventRepository.findById(500L)).thenReturn(Optional.of(eventEntity));
     
        Event result = eventService.findEventById(500L);

        assertNotNull(result);
        assertEquals(500L, result.getId());
        assertEquals(organizer.getId(), result.getOrganizer().getId());
    }

    @Test
    void findEventById_ShouldThrowDataNotFound_WhenIdDoesNotExist() {
        
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        DataNotFound exception = assertThrows(DataNotFound.class, () -> {
            eventService.findEventById(999L);
        });

        assertEquals("No existe un evento con ese id", exception.getMessage());
    }
    
    /**@Test
    void getAllEvents_ShouldReturnListOfDTOs() {
      
        List<Event> events = List.of(eventEntity);
        when(eventRepository.findAll()).thenReturn(events);

        // Act
        List<EventResponseDTO> result = eventService.getAllEvents();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        
        verify(eventMapper, times(1)).toDTO(any(Event.class));
        verify(eventRepository).findAll();
    }
    **/
}