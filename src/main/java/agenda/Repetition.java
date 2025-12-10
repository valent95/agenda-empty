package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Repetition {
    private final ChronoUnit myFrequency;
    private final List<LocalDate> exceptions = new ArrayList<>();
    private Termination termination;

    public Repetition(ChronoUnit myFrequency) {
        this.myFrequency = myFrequency;
    }

    public ChronoUnit getFrequency() {
        return myFrequency;
    }

    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public boolean isException(LocalDate date) {
        return exceptions.contains(date);
    }

    public Termination getTermination() {
        return termination;
    }
}