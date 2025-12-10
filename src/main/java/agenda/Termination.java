package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Termination {

    private final LocalDate start;
    private final ChronoUnit frequency;
    private final LocalDate terminationDate;
    private final long numberOfOccurrences;

    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        this.start = start;
        this.frequency = frequency;
        this.terminationDate = terminationInclusive;
        this.numberOfOccurrences = frequency.between(start, terminationInclusive) + 1;
    }

    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        this.start = start;
        this.frequency = frequency;
        this.numberOfOccurrences = numberOfOccurrences;
        if (numberOfOccurrences > 0) {
            this.terminationDate = start.plus(numberOfOccurrences - 1, frequency);
        } else {
            this.terminationDate = start; 
        }
    }

    public LocalDate terminationDateInclusive() {
        return terminationDate;
    }

    public long numberOfOccurrences() {
        return numberOfOccurrences;
    }
}