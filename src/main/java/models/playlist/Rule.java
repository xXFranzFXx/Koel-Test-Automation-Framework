package models.playlist;

public class Rule {
    private long id;
    private InnerRule[] innerRules;
    public Rule(InnerRule[] innerRules) {
        this.innerRules = innerRules;
    }
    public Rule() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public InnerRule[] getRules() {
        return innerRules;
    }
    public void setRules(InnerRule[] innerRules) {
        this.innerRules = innerRules;
    }
}
