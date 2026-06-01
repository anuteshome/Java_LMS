package com.library.model;

import javafx.beans.property.*;

public class Member {

    private final IntegerProperty id       = new SimpleIntegerProperty();
    private final StringProperty  name     = new SimpleStringProperty();
    private final StringProperty  email    = new SimpleStringProperty();
    private final StringProperty  phone    = new SimpleStringProperty();
    private final StringProperty  address  = new SimpleStringProperty();
    private final BooleanProperty isActive = new SimpleBooleanProperty();

    public Member() {}

    public Member(int id, String name, String email,
                  String phone, String address, boolean isActive) {
        setId(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setAddress(address);
        setActive(isActive);
    }

    // --- ID ---
    public int getId()                      { return id.get(); }
    public void setId(int v)                { id.set(v); }
    public IntegerProperty idProperty()     { return id; }

    // --- Name ---
    public String getName()                 { return name.get(); }
    public void setName(String v)           { name.set(v); }
    public StringProperty nameProperty()    { return name; }

    // --- Email ---
    public String getEmail()                { return email.get(); }
    public void setEmail(String v)          { email.set(v); }
    public StringProperty emailProperty()   { return email; }

    // --- Phone ---
    public String getPhone()                { return phone.get(); }
    public void setPhone(String v)          { phone.set(v); }
    public StringProperty phoneProperty()   { return phone; }

    // --- Address ---
    public String getAddress()              { return address.get(); }
    public void setAddress(String v)        { address.set(v); }
    public StringProperty addressProperty() { return address; }

    // --- Active ---
    public boolean isActive()               { return isActive.get(); }
    public void setActive(boolean v)        { isActive.set(v); }
    public BooleanProperty activeProperty() { return isActive; }

    @Override
    public String toString() { return name.get() + " (" + email.get() + ")"; }
}
