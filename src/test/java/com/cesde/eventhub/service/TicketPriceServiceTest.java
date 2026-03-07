package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cesde.eventhub.dto.request.TicketPriceRegisterDTO;
import com.cesde.eventhub.dto.response.TicketPriceResponseDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.entity.TicketPrice;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.entity.Zone;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.TicketPriceMapper;
import com.cesde.eventhub.repository.TicketPriceRepository;

@ExtendWith(MockitoExtension.class)
class TicketPriceServiceTest {

    @Mock private ZoneService zoneService;
    @Mock private EventService eventService;
    @Mock private PlaceService placeService;
    @Mock private UserService userService;
    @Mock private TicketPriceRepository ticketPriceRepository;
    
    @Spy private final TicketPriceMapper ticketMapper = Mappers.getMapper(TicketPriceMapper.class);

    @InjectMocks
    private TicketPriceService ticketPriceService;

  
    private Event event;
    private Zone zone;
    private Place place;
    private User organizer;
    private TicketPriceRegisterDTO registerDTO;
    private TicketPrice ticketPriceEntity;

    @BeforeEach
    void setUp() {
        
        organizer = new User();
        organizer.setId(UUID.randomUUID());

        event = new Event();
        event.setId(1L);
        event.setOrganizer(organizer);

        place = new Place();
        place.setId(10L);
        place.setName("Teatro Municipal");

        zone = new Zone();
        zone.setId(20L);
        zone.setCapacity(100);
        zone.setPlace(place);

   
        registerDTO = new TicketPriceRegisterDTO();
        registerDTO.setEventId(1L);
        registerDTO.setZoneId(20L);
        registerDTO.setAvailableQuantity(50); // Menor que la capacidad (100)
        registerDTO.setPrice(150.0);

       
        ticketPriceEntity = new TicketPrice();
        ticketPriceEntity.setId(100L);
        ticketPriceEntity.setAvailableQuantity(50);
    }

    @Test
    void createTicketPrice_ShouldSaveSuccessfully() {
    
        when(eventService.findEventById(1L)).thenReturn(event);
        when(zoneService.findById(20L)).thenReturn(zone);
        when(ticketPriceRepository.save(any(TicketPrice.class))).thenReturn(ticketPriceEntity);
 
        TicketPriceResponseDTO result = ticketPriceService.createTicketPrice(registerDTO);

        assertNotNull(result);
        verify(userService).validateAuthority(organizer.getId());
        verify(placeService).validatePlaceIsActiveAndExists(place.getId());
        verify(ticketPriceRepository).save(any(TicketPrice.class));
    }

    @Test
    void validateAvaliableQuantity_ShouldThrowException_WhenQuantityExceedsCapacity() {
    
        registerDTO.setAvailableQuantity(150);
        
        InvalidRegistration exception = assertThrows(InvalidRegistration.class, () -> {
            ticketPriceService.validateAvaliableQuantity(registerDTO, zone);
        });

        assertTrue(exception.getMessage().contains("no puede superar a la capacidad de la zona"));
    }

    @Test
    void getPricesByEvent_ShouldReturnList() {
      
        List<TicketPrice> prices = List.of(ticketPriceEntity);
        when(ticketPriceRepository.findAllByEventId(1L)).thenReturn(prices);

        List<TicketPriceResponseDTO> result = ticketPriceService.getPricesByEvent(1L);
      
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventService).findEventById(1L);
    }
}