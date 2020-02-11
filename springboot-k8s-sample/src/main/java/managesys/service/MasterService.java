package managesys.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import managesys.model.Category;
import managesys.model.Format;
import managesys.repository.BookRepository;

@Service("masterLogic")
public class MasterService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public List<Category> findAllCategories() {
        return Category.findAll(bookRepository);
    }

    @Transactional
    public void registerCategory(Category category) {
        Optional<Category> c = Category.findByName(bookRepository, category.getName());
        if (!c.isPresent()) {
            category.save(bookRepository);
        }
    }

    @Transactional
    public Optional<Category> getCategory(int id) {
        return Category.findById(bookRepository, id);
    }

    @Transactional
    public List<Format> findAllFormats() {
        return Format.findAll(bookRepository);
    }

    @Transactional
    public void registerFormat(Format format) {
        Optional<Format> f = Format.findByName(bookRepository, format.getName());
        if (!f.isPresent()) {
            format.save(bookRepository);
        }
    }

    @Transactional
    public Optional<Format> getFormat(int id) {
        return Format.findById(bookRepository, id);
    }

}
