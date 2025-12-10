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
            LocalDateTime endDateTime = myStart.plus(myDuration);
            LocalDate start = myStart.toLocalDate();
            LocalDate end = endDateTime.toLocalDate();
            return !aDay.isBefore(start) && !aDay.isAfter(end);
        }

        // Cas 2 : Événement répétitif
        LocalDate startDate = myStart.toLocalDate();
        ChronoUnit frequency = repetition.getFrequency();

        // Si la date demandée est avant le tout début de l'événement, c'est faux
        if (aDay.isBefore(startDate)) {
            return false;
        }

        // On cherche l'occurrence qui aurait commencé au plus près de aDay
        long units = frequency.between(startDate, aDay);
        LocalDate occurrenceDate = startDate.plus(units, frequency);

        // a. Vérifier si cette occurrence est une exception
        if (repetition.isException(occurrenceDate)) {
            return false;
        }

        // b. Vérifier la terminaison (CRITIQUE : on vérifie la date de DÉBUT de l'occurrence)
        Termination termination = repetition.getTermination();
        if (termination != null) {
            LocalDate termDate = termination.terminationDateInclusive();
            // Si l'occurrence commence APRÈS la date de fin prévue, c'est faux
            if (termDate != null && occurrenceDate.isAfter(termDate)) {
                return false;
            }
        }

        // c. Vérifier si l'occurrence couvre effectivement aDay (gestion du débordement)
        LocalDateTime occurrenceStart = occurrenceDate.atTime(myStart.toLocalTime());
        LocalDateTime occurrenceEnd = occurrenceStart.plus(myDuration);
        
        LocalDate s = occurrenceStart.toLocalDate();
        LocalDate e = occurrenceEnd.toLocalDate();

        // L'événement a lieu si aDay est compris entre le début et la fin de cette occurrence
        return !aDay.isBefore(s) && !aDay.isAfter(e);
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
        return String.format("Event{title='%s', start=%s, duration=%s}", myTitle, myStart, myDuration);
    }
}