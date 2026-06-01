package com.library.model;

import javafx.beans.property.*;

public class Book {

    private final IntegerProperty id        = new SimpleIntegerProperty();
    private final StringProperty  title     = new SimpleStringProperty();
    private final StringProperty  author    = new SimpleStringProperty();
    private final StringProperty  isbn      = new SimpleStringProperty();
    private final StringProperty  category  = new SimpleStringProperty();
    private final IntegerProperty quantity  = new SimpleIntegerProperty();
    private final IntegerProperty available = new SimpleIntegerProperty();

    // Constructors
    public Book() {}

    public Book(int id, String title, String author, String isbn,
                String category, int quantity, int available) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        setCategory(category);
        setQuantity(quantity);
        setAvailable(available);
    }

    // --- ID ---
    public int getId()                      { return id.get(); }
    public void setId(int v)                { id.set(v); }
    public IntegerProperty idProperty()     { return id; }

    // --- Title ---
    public String getTitle()                { return title.get(); }
    public void setTitle(String v)          { title.set(v); }
    public StringProperty titleProperty()   { return title; }

    // --- Author ---
    public String getAuthor()               { return author.get(); }
    public void setAuthor(String v)         { author.set(v); }
    public StringProperty authorProperty()  { return author; }

    // --- ISBN ---
    public String getIsbn()                 { return isbn.get(); }
    public void setIsbn(String v)           { isbn.set(v); }
    public StringProperty isbnProperty()    { return isbn; }

    // --- Category ---
    public String getCategory()             { return category.get(); }
    public void setCategory(String v)       { category.set(v); }
    public StringProperty categoryProperty(){ return category; }

    // --- Quantity ---
    public int getQuantity()                { return quantity.get(); }
    public void setQuantity(int v)          { quantity.set(v); }
    public IntegerProperty quantityProperty(){ return quantity; }

    // --- Available ---
    public int getAvailable()               { return available.get(); }
    public void setAvailable(int v)         { available.set(v); }
    public IntegerProperty availableProperty(){ return available; }

    @Override
    public String toString() {
        return title.get() + " by " + author.get();
    }
}
