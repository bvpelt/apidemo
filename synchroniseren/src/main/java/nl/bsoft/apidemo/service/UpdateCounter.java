package nl.bsoft.apidemo.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCounter {
    private int added;
    private int updated;
    private int unmodified;
    private int removed;
    private int skipped;
    private int processed;

    public void add() {
        processed();
        this.added++;
    }

    public void unmodified() {
        processed();
        this.unmodified++;
    }

    public void updated() {
        processed();
        this.updated++;
    }

    public void removed() {
        processed();
        this.removed++;
    }

    public void skipped() {
        processed();
        this.skipped++;
    }

    private void processed() {
        this.processed++;
    }
}
