package managesys.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import configuration.JpaTestConfiguration;
import managesys.model.Category;
import managesys.model.Format;
import managesys.repository.BookRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasterService.class)
@ContextConfiguration(classes = JpaTestConfiguration.class)
public class MasterServiceTest {

    @Autowired
    private MasterService masterService;

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void setup() {
        Category c1 = new Category("c1");
        c1.save(bookRepository);
        Category c2 = new Category("c2");
        c2.save(bookRepository);
        Category c3 = new Category("c3");
        c3.save(bookRepository);

        Format f1 = new Format("f1");
        f1.save(bookRepository);
        Format f2 = new Format("f2");
        f2.save(bookRepository);
        Format f3 = new Format("f3");
        f3.save(bookRepository);

        bookRepository.flush();
    }

    @Test
    @Transactional
    public void findAllCategories() {
        assertEquals(masterService.findAllCategories().size(), 3);
    }

    @Test
    @Transactional
    public void registerCategoryExist() {
        Category category = new Category("c3");
        masterService.registerCategory(category);

        bookRepository.flush();
        assertEquals(Category.findAll(bookRepository).size(), 3);
    }

    @Test
    @Transactional
    public void registerCategory() {
        Category category = new Category("c4");
        masterService.registerCategory(category);

        bookRepository.flush();
        assertEquals(Category.findAll(bookRepository).size(), 4);
    }

    @Test
    @Transactional
    public void getCategory() {
        List<Category> list = Category.findAll(bookRepository);

        Optional<Category> optc = masterService.getCategory(list.get(0).getId());

        assertTrue(optc.isPresent());
        assertEquals(optc.get().getName(), "c1");
    }

    @Test
    @Transactional
    public void findAllFormats() {
        assertEquals(masterService.findAllFormats().size(), 3);
    }

    @Test
    @Transactional
    public void registerFormatExist() {
        Format format = new Format("f3");
        masterService.registerFormat(format);

        bookRepository.flush();
        assertEquals(Format.findAll(bookRepository).size(), 3);
    }

    @Test
    @Transactional
    public void registerFormat() {
        Format format = new Format("c4");
        masterService.registerFormat(format);

        bookRepository.flush();
        assertEquals(Format.findAll(bookRepository).size(), 4);
    }

    @Test
    @Transactional
    public void getFormat() {
        List<Format> list = Format.findAll(bookRepository);

        Optional<Format> optf = masterService.getFormat(list.get(0).getId());

        assertTrue(optf.isPresent());
        assertEquals(optf.get().getName(), "f1");
    }
}
