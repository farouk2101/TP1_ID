package main;

import java.util.ArrayList;

public class Column {
    private Type type;
    private int maxSize;
    private String name;
    private boolean primaryKey;
    private boolean notNull;
    private ArrayList<String> items;

    public Column(String name){
        items = new ArrayList<>();
        setName(name);
        setType(Type.UNKNOWN);
        setMaxSize(0);
    }

    public void addItem(String item) {
        items.add(item);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public Type getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
}
