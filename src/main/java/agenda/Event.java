package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Event {
    @SuppressWarnings("FieldMayBeFinal")
    private String myTitle;
    @SuppressWarnings("FieldMayBeFinal")
    private LocalDateTime myStart;
    @SuppressWarnings("FieldMayBeFinal")
    private Duration myDuration;
    private Repetition repetition;

    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        if (repetition != null) {
            repetition.addException(date);
        }
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (repetition != null) {
            repetition.setTermination(new Termination(
                    myStart.toLocalDate(),
                    repetition.getFrequency(),
                    terminationInclusive
            ));
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            repetition.setTermination(new Termination(
                    myStart.toLocalDate(),
                    repetition.getFrequency(),
                    numberOfOccurrences
            ));
        }
    }

    public int getNumberOfOccurrences() {
        if (repetition != null && repetition.getTermination() != null) {
            return (int) repetition.getTermination().numberOfOccurrences();
        }
        return 0;
    }

    public LocalDate getTerminationDate() {
        if (repetition != null && repetition.getTermination() != null) {
            return repetition.getTermination().terminationDateInclusive();
        }
        return null;
    }

    public boolean isInDay(LocalDate aDay) {
        // Cas 1 : Événement non répétitif
        if (repetition == null) {
            LocalDate startDate = myStart.toLocalDate();
            // La date de fin tient compte de la durée (pour les événements qui passent minuit)
            LocalDate endDate = myStart.plus(myDuration).toLocalDate();
            
            // L'événement a lieu si aDay est compris entre startDate et endDate (inclus)
            return !aDay.isBefore(startDate) && !aDay.isAfter(endDate);
        }

        // Cas 2 : Événement répétitif
        
        // a. Vérifier si c'est une exception
        if (repetition.isException(aDay)) {
            return false;
        }

        LocalDate startDate = myStart.toLocalDate();

        // b. Vérifier si la date est avant le début de l'événement
        if (aDay.isBefore(startDate)) {
            return false;
        }

        // c. Vérifier la date de fin (Termination) si elle existe
        Termination termination = repetition.getTermination();
        if (termination != null) {
            if (aDay.isAfter(termination.terminationDateInclusive())) {
                return false;
            }
        }

        // d. Vérifier la fréquence (C'est ici que résidait l'erreur principale)
        // On calcule le nombre d'unités de temps entre le début et le jour testé
        long units = repetition.getFrequency().between(startDate, aDay);
        // On vérifie si en ajoutant ces unités au départ, on retombe exactement sur aDay
        LocalDate calculatedDate = startDate.plus(units, repetition.getFrequency());
        
        return calculatedDate.equals(aDay);
    }

    public String getTitle() {
        return myTitle;
    }

    public LocalDateTime getStart() {
        return myStart;
    }

    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}