package managesys.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import managesys.repository.AbstractRepository;

@Entity(name = "CATEGORY_MASTER")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    public Category() {
    }

    public Category(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        super();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Optional<Category> findById(AbstractRepository repo, int id) {
        return repo.findById(Category.class, id);
    }

    public static List<Category> findAll(AbstractRepository repo) {
        return repo.findAll(Category.class);
    }

    public void save(AbstractRepository repo) {
        repo.save(Category.class, this);
    }

    public static Optional<Category> findByName(AbstractRepository repo, String name) {
        return repo.findOne("from CATEGORY_MASTER  where name = ?1", Category.class, name);
    }
}
