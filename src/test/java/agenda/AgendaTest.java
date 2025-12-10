package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgendaTest {
    Agenda agenda;

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1_2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement simple
    // November 1st, 2020, 22:30, 120 minutes
    Event simple;

    // Un événement qui se répète toutes les semaines et se termine à une date
    // donnée
    Event fixedTermination;

    // Un événement qui se répète toutes les semaines et se termine après un nombre
    // donné d'occurrences
    Event fixedRepetitions;

    // A daily repetitive event, never ending
    // Un événement répétitif quotidien, sans fin
    // November 1st, 2020, 22:30, 120 minutes
    Event neverEnding;

    @BeforeEach
    public void setUp() {
        simple = new Event("Simple event", nov_1_2020_22_30, min_120);

        fixedTermination = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedTermination.setRepetition(ChronoUnit.WEEKS);
        fixedTermination.setTermination(jan_5_2021);

        fixedRepetitions = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedRepetitions.setRepetition(ChronoUnit.WEEKS);
        fixedRepetitions.setTermination(10);

        neverEnding = new Event("Never Ending", nov_1_2020_22_30, min_120);
        neverEnding.setRepetition(ChronoUnit.DAYS);

        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }

    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(),
                "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
    }

    @Test
    public void testFindByTitle() {
        // Test: trouver un événement par titre exact
        var results = agenda.findByTitle("Simple event");
        assertEquals(1, results.size(), "Doit trouver un événement avec le titre 'Simple event'");
        assertTrue(results.contains(simple), "Le résultat doit contenir l'événement simple");

        // Test: trouver plusieurs événements avec le même titre
        var fixedResults = agenda.findByTitle("Fixed termination weekly");
        assertEquals(2, fixedResults.size(), "Doit trouver 2 événements avec le titre 'Fixed termination weekly'");
        assertTrue(fixedResults.contains(fixedTermination), "Doit contenir fixedTermination");
        assertTrue(fixedResults.contains(fixedRepetitions), "Doit contenir fixedRepetitions");

        // Test: aucun événement trouvé
        var noneFound = agenda.findByTitle("Non existent event");
        assertEquals(0, noneFound.size(), "Doit retourner une liste vide pour un titre non existant");
    }

    @Test
    public void testIsFreeForNonOverlappingEvent() {
        // Test: un événement qui ne chevauche pas les autres
        LocalDateTime nov_2_2020_10_00 = LocalDateTime.of(2020, 11, 2, 10, 0);
        Event nonOverlappingEvent = new Event("Free slot", nov_2_2020_10_00, Duration.ofMinutes(60));
        
        assertTrue(agenda.isFreeFor(nonOverlappingEvent), 
                "L'agenda doit être libre pour un événement sans chevauchement");
    }

    

}
