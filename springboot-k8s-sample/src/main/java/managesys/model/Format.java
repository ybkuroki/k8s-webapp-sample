package managesys.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import managesys.repository.AbstractRepository;

@Entity(name = "FORMAT_MASTER")
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    public Format() {
    }

    public Format(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Format(String name) {
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

    public static Optional<Format> findById(AbstractRepository repo, int id) {
        return repo.findById(Format.class, id);
    }

    public static List<Format> findAll(AbstractRepository repo) {
        return repo.findAll(Format.class);
    }

    public void save(AbstractRepository repo) {
        repo.save(Format.class, this);
    }

    public static Optional<Format> findByName(AbstractRepository repo, String name) {
        return repo.findOne("from FORMAT_MASTER where name = ?1", Format.class, name);
    }

}