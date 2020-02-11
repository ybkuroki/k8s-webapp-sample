package managesys.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import managesys.repository.AbstractRepository;

@Entity(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 3, max = 50)
    private String title;

    @Size(min = 10, max = 20)
    private String isbn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FORMAT_ID")
    private Format format;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public Book() {

    }

    public Book(String title, String isbn, Category category, Format format) {
        super();
        this.title = title;
        this.isbn = isbn;
        this.category = category;
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Optional<Book> findById(AbstractRepository repo, int id) {
        return repo.findById(Book.class, id);
    }

    public void save(AbstractRepository repo) {
        repo.save(Book.class, this);
    }

    public void delete(AbstractRepository repo) {
        repo.delete(this);
    }

    public static List<Book> findAll(AbstractRepository repo) {
        return repo.find("select b from BOOK b left join fetch b.category left join fetch b.format", Book.class);
    }

    public static Page<Book> findAll(AbstractRepository repo, Pageable pageable) {
        return repo.find("select b from BOOK b left join fetch b.category left join fetch b.format", Book.class, pageable);
    }

    public static Optional<Book> findByIsbn(AbstractRepository repo, String isbn) {
        String jpql = "from BOOK b left join fetch b.category left join fetch b.format where b.isbn = ?1";
        return repo.findOne(jpql, Book.class, isbn);
    }

    public static Page<Book> findByTitle(AbstractRepository repo, String title, Pageable pageable) {
        String jpql = "from BOOK b left join fetch b.category left join fetch b.format where b.title like ?1";
        return repo.find(jpql, Book.class, pageable, "%" + title + "%");
    }
}
