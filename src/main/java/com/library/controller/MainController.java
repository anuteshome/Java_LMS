package com.library.controller;

import com.library.database.BookDAO;
import com.library.database.BorrowDAO;
import com.library.database.MemberDAO;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // =========================================================
    // FXML INJECTIONS - Books
    // =========================================================
    @FXML private TextField     bookSearchField;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> bookIdCol;
    @FXML private TableColumn<Book, String>  bookTitleCol, bookAuthorCol, bookIsbnCol, bookCategoryCol;
    @FXML private TableColumn<Book, Integer> bookQtyCol, bookAvailCol;

    @FXML private TitledPane bookFormPane;
    @FXML private TextField  bookTitleField, bookAuthorField, bookIsbnField, bookCategoryField, bookQtyField;

    // =========================================================
    // FXML INJECTIONS - Members
    // =========================================================
    @FXML private TextField        memberSearchField;
    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, Integer> memberIdCol;
    @FXML private TableColumn<Member, String>  memberNameCol, memberEmailCol, memberPhoneCol, memberAddrCol;
    @FXML private TableColumn<Member, Boolean> memberActiveCol;

    @FXML private TitledPane memberFormPane;
    @FXML private TextField  memberNameField, memberEmailField, memberPhoneField, memberAddrField;

    // =========================================================
    // FXML INJECTIONS - Borrow
    // =========================================================
    @FXML private ComboBox<Book>   borrowBookCombo;
    @FXML private ComboBox<Member> borrowMemberCombo;
    @FXML private DatePicker       dueDatePicker;

    @FXML private TableView<BorrowRecord> borrowTable;
    @FXML private TableColumn<BorrowRecord, Integer> brIdCol;
    @FXML private TableColumn<BorrowRecord, String>  brBookCol, brMemberCol, brBorrowDateCol,
                                                     brDueDateCol, brReturnDateCol, brStatusCol;

    // =========================================================
    // FXML INJECTIONS - Status
    // =========================================================
    @FXML private Label infoLabel;
    @FXML private Label statusLabel;

    // Tracks edit mode
    private boolean isEditingBook   = false;
    private boolean isEditingMember = false;
    private int     editingBookId   = -1;
    private int     editingMemberId = -1;

    // =========================================================
    // INITIALIZE
    // =========================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupBookTable();
        setupMemberTable();
        setupBorrowTable();
        loadAllBooks();
        loadAllMembers();
        loadBorrowRecords();
        dueDatePicker.setValue(LocalDate.now().plusDays(14));
    }

    // =========================================================
    // BOOK TABLE SETUP
    // =========================================================
    private void setupBookTable() {
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookIsbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        bookQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        bookAvailCol.setCellValueFactory(new PropertyValueFactory<>("available"));
    }

    // =========================================================
    // MEMBER TABLE SETUP
    // =========================================================
    private void setupMemberTable() {
        memberIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        memberNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        memberEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        memberPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        memberAddrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        memberActiveCol.setCellValueFactory(new PropertyValueFactory<>("active"));
    }

    // =========================================================
    // BORROW TABLE SETUP
    // =========================================================
    private void setupBorrowTable() {
        brIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        brBookCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        brMemberCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        brBorrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        brDueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        brReturnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        brStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    // =========================================================
    // BOOKS - Load / Search
    // =========================================================
    @FXML
    public void loadAllBooks() {
        ObservableList<Book> books = BookDAO.getAllBooks();
        booksTable.setItems(books);
        // Refresh combos in borrow tab
        borrowBookCombo.setItems(books);
        setInfo("Loaded " + books.size() + " books.");
    }

    @FXML
    private void searchBooks() {
        String kw = bookSearchField.getText().trim();
        if (kw.isEmpty()) { loadAllBooks(); return; }
        ObservableList<Book> result = BookDAO.searchBooks(kw);
        booksTable.setItems(result);
        setInfo("Found " + result.size() + " books for: \"" + kw + "\"");
    }

    // =========================================================
    // BOOKS - Show Add / Edit Forms
    // =========================================================
    @FXML
    private void showAddBook() {
        isEditingBook = false;
        editingBookId = -1;
        clearBookForm();
        bookFormPane.setExpanded(true);
        bookFormPane.setText("➕ Add New Book");
    }

    @FXML
    private void showEditBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a book to edit."); return; }
        isEditingBook = true;
        editingBookId = selected.getId();
        bookTitleField.setText(selected.getTitle());
        bookAuthorField.setText(selected.getAuthor());
        bookIsbnField.setText(selected.getIsbn());
        bookCategoryField.setText(selected.getCategory());
        bookQtyField.setText(String.valueOf(selected.getQuantity()));
        bookFormPane.setExpanded(true);
        bookFormPane.setText("✏ Edit Book: " + selected.getTitle());
    }

    // =========================================================
    // BOOKS - Save (Add or Update)
    // =========================================================
    @FXML
    private void saveBook() {
        String title    = bookTitleField.getText().trim();
        String author   = bookAuthorField.getText().trim();
        String isbn     = bookIsbnField.getText().trim();
        String category = bookCategoryField.getText().trim();
        String qtyStr   = bookQtyField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert("Title, Author, and ISBN are required.");
            return;
        }
        int qty;
        try { qty = Integer.parseInt(qtyStr); }
        catch (NumberFormatException e) { showAlert("Quantity must be a number."); return; }

        boolean success;
        if (isEditingBook) {
            Book b = new Book(editingBookId, title, author, isbn, category, qty, qty);
            success = BookDAO.updateBook(b);
            setInfo(success ? "✅ Book updated successfully." : "❌ Update failed.");
        } else {
            Book b = new Book(0, title, author, isbn, category, qty, qty);
            success = BookDAO.addBook(b);
            setInfo(success ? "✅ Book added successfully." : "❌ Add failed (duplicate ISBN?).");
        }
        if (success) {
            clearBookForm();
            bookFormPane.setExpanded(false);
            loadAllBooks();
        }
    }

    @FXML private void cancelBook()  { clearBookForm(); bookFormPane.setExpanded(false); }
    private void clearBookForm() {
        bookTitleField.clear(); bookAuthorField.clear();
        bookIsbnField.clear();  bookCategoryField.clear(); bookQtyField.clear();
    }

    // =========================================================
    // BOOKS - Delete
    // =========================================================
    @FXML
    private void deleteBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a book to delete."); return; }
        if (confirmDialog("Delete book: \"" + selected.getTitle() + "\"?")) {
            boolean ok = BookDAO.deleteBook(selected.getId());
            setInfo(ok ? "✅ Book deleted." : "❌ Delete failed.");
            if (ok) loadAllBooks();
        }
    }

    // =========================================================
    // MEMBERS - Load / Search
    // =========================================================
    @FXML
    public void loadAllMembers() {
        ObservableList<Member> members = MemberDAO.getAllMembers();
        membersTable.setItems(members);
        borrowMemberCombo.setItems(members);
        setInfo("Loaded " + members.size() + " members.");
    }

    @FXML
    private void searchMembers() {
        String kw = memberSearchField.getText().trim();
        if (kw.isEmpty()) { loadAllMembers(); return; }
        ObservableList<Member> result = MemberDAO.searchMembers(kw);
        membersTable.setItems(result);
        setInfo("Found " + result.size() + " members for: \"" + kw + "\"");
    }

    // =========================================================
    // MEMBERS - Show Add / Edit
    // =========================================================
    @FXML
    private void showAddMember() {
        isEditingMember = false;
        editingMemberId = -1;
        clearMemberForm();
        memberFormPane.setExpanded(true);
        memberFormPane.setText("➕ Add New Member");
    }

    @FXML
    private void showEditMember() {
        Member selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a member to edit."); return; }
        isEditingMember = true;
        editingMemberId = selected.getId();
        memberNameField.setText(selected.getName());
        memberEmailField.setText(selected.getEmail());
        memberPhoneField.setText(selected.getPhone());
        memberAddrField.setText(selected.getAddress());
        memberFormPane.setExpanded(true);
        memberFormPane.setText("✏ Edit Member: " + selected.getName());
    }

    // =========================================================
    // MEMBERS - Save
    // =========================================================
    @FXML
    private void saveMember() {
        String name  = memberNameField.getText().trim();
        String email = memberEmailField.getText().trim();
        String phone = memberPhoneField.getText().trim();
        String addr  = memberAddrField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            showAlert("Name and Email are required.");
            return;
        }
        boolean success;
        if (isEditingMember) {
            Member m = new Member(editingMemberId, name, email, phone, addr, true);
            success = MemberDAO.updateMember(m);
            setInfo(success ? "✅ Member updated." : "❌ Update failed.");
        } else {
            Member m = new Member(0, name, email, phone, addr, true);
            success = MemberDAO.addMember(m);
            setInfo(success ? "✅ Member added." : "❌ Add failed (duplicate email?).");
        }
        if (success) {
            clearMemberForm();
            memberFormPane.setExpanded(false);
            loadAllMembers();
        }
    }

    @FXML private void cancelMember()  { clearMemberForm(); memberFormPane.setExpanded(false); }
    private void clearMemberForm() {
        memberNameField.clear(); memberEmailField.clear();
        memberPhoneField.clear(); memberAddrField.clear();
    }

    // =========================================================
    // MEMBERS - Delete
    // =========================================================
    @FXML
    private void deleteMember() {
        Member selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a member to delete."); return; }
        if (confirmDialog("Delete member: \"" + selected.getName() + "\"?")) {
            boolean ok = MemberDAO.deleteMember(selected.getId());
            setInfo(ok ? "✅ Member deleted." : "❌ Delete failed.");
            if (ok) loadAllMembers();
        }
    }

    // =========================================================
    // BORROW - Load Records
    // =========================================================
    @FXML
    public void loadBorrowRecords() {
        ObservableList<BorrowRecord> records = BorrowDAO.getAllBorrowRecords();
        borrowTable.setItems(records);
        setInfo("Loaded " + records.size() + " borrow records.");
    }

    // =========================================================
    // BORROW - Borrow a Book
    // =========================================================
    @FXML
    private void borrowBook() {
        Book   book   = borrowBookCombo.getSelectionModel().getSelectedItem();
        Member member = borrowMemberCombo.getSelectionModel().getSelectedItem();
        LocalDate due = dueDatePicker.getValue();

        if (book == null || member == null || due == null) {
            showAlert("Please select a book, member, and due date.");
            return;
        }
        if (book.getAvailable() <= 0) {
            showAlert("No copies available for: " + book.getTitle());
            return;
        }
        boolean ok = BorrowDAO.borrowBook(book.getId(), member.getId(), due.toString());
        if (ok) {
            setInfo("✅ Book issued: \"" + book.getTitle() + "\" to " + member.getName());
            loadAllBooks();
            loadBorrowRecords();
        } else {
            setInfo("❌ Failed to issue book.");
        }
    }

    // =========================================================
    // BORROW - Return a Book
    // =========================================================
    @FXML
    private void returnBook() {
        BorrowRecord selected = borrowTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a borrow record to return."); return; }
        if ("RETURNED".equals(selected.getStatus())) {
            showAlert("This book has already been returned.");
            return;
        }
        if (confirmDialog("Return book: \"" + selected.getBookTitle() + "\"?")) {
            boolean ok = BorrowDAO.returnBook(selected.getId());
            if (ok) {
                setInfo("✅ Book returned: \"" + selected.getBookTitle() + "\"");
                loadAllBooks();
                loadBorrowRecords();
            } else {
                setInfo("❌ Return failed.");
            }
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private void setInfo(String msg) {
        if (infoLabel != null) infoLabel.setText(msg);
        System.out.println("[INFO] " + msg);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean confirmDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
