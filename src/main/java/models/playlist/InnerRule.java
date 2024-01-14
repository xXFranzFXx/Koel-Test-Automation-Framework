package models.playlist;
public class InnerRule {
    private long id;
    private String model;
    private String operator;
    private String[] value;
    public InnerRule(String model, String operator, String[] value) {
        this.model = model;
        this.operator = operator;
        this.value = value;
    }

    public InnerRule() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }
}
