package com.library.model;

import javafx.beans.property.*;

public class BorrowRecord {

    private final IntegerProperty id           = new SimpleIntegerProperty();
    private final IntegerProperty bookId       = new SimpleIntegerProperty();
    private final IntegerProperty memberId     = new SimpleIntegerProperty();
    private final StringProperty  bookTitle    = new SimpleStringProperty();
    private final StringProperty  memberName   = new SimpleStringProperty();
    private final StringProperty  borrowDate   = new SimpleStringProperty();
    private final StringProperty  dueDate      = new SimpleStringProperty();
    private final StringProperty  returnDate   = new SimpleStringProperty();
    private final StringProperty  status       = new SimpleStringProperty();

    public BorrowRecord() {}

    public BorrowRecord(int id, int bookId, int memberId,
                        String bookTitle, String memberName,
                        String borrowDate, String dueDate,
                        String returnDate, String status) {
        setId(id);
        setBookId(bookId);
        setMemberId(memberId);
        setBookTitle(bookTitle);
        setMemberName(memberName);
        setBorrowDate(borrowDate);
        setDueDate(dueDate);
        setReturnDate(returnDate);
        setStatus(status);
    }

    public int getId()                          { return id.get(); }
    public void setId(int v)                    { id.set(v); }
    public IntegerProperty idProperty()         { return id; }

    public int getBookId()                      { return bookId.get(); }
    public void setBookId(int v)                { bookId.set(v); }
    public IntegerProperty bookIdProperty()     { return bookId; }

    public int getMemberId()                    { return memberId.get(); }
    public void setMemberId(int v)              { memberId.set(v); }
    public IntegerProperty memberIdProperty()   { return memberId; }

    public String getBookTitle()                { return bookTitle.get(); }
    public void setBookTitle(String v)          { bookTitle.set(v); }
    public StringProperty bookTitleProperty()   { return bookTitle; }

    public String getMemberName()               { return memberName.get(); }
    public void setMemberName(String v)         { memberName.set(v); }
    public StringProperty memberNameProperty()  { return memberName; }

    public String getBorrowDate()               { return borrowDate.get(); }
    public void setBorrowDate(String v)         { borrowDate.set(v); }
    public StringProperty borrowDateProperty()  { return borrowDate; }

    public String getDueDate()                  { return dueDate.get(); }
    public void setDueDate(String v)            { dueDate.set(v); }
    public StringProperty dueDateProperty()     { return dueDate; }

    public String getReturnDate()               { return returnDate.get(); }
    public void setReturnDate(String v)         { returnDate.set(v); }
    public StringProperty returnDateProperty()  { return returnDate; }

    public String getStatus()                   { return status.get(); }
    public void setStatus(String v)             { status.set(v); }
    public StringProperty statusProperty()      { return status; }
}
