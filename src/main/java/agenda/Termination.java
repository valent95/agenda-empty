package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Termination {

    private final LocalDate start;
    private final ChronoUnit frequency;
    private final LocalDate terminationDate;
    private final long numberOfOccurrences;

    /**
     * Constructeur par date de fin : calcule automatiquement le nombre d'occurrences.
     */
    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        this.start = start;
        this.frequency = frequency;
        this.terminationDate = terminationInclusive;
        // On calcule le nombre d'unités complètes + 1 pour inclure le démarrage
        this.numberOfOccurrences = frequency.between(start, terminationInclusive) + 1;
    }

    /**
     * Constructeur par nombre d'occurrences : calcule automatiquement la date de fin.
     */
    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        this.start = start;
        this.frequency = frequency;
        this.numberOfOccurrences = numberOfOccurrences;
        // La date de fin est : DateDébut + (N-1) * Fréquence
        if (numberOfOccurrences > 0) {
            this.terminationDate = start.plus(numberOfOccurrences - 1, frequency);
        } else {
            this.terminationDate = start.minus(1, frequency); // Cas théorique 0 occurrence
        }
    }

    public LocalDate terminationDateInclusive() {
        return terminationDate;
    }

    public long numberOfOccurrences() {
        return numberOfOccurrences;
    }
}