package managesys;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import configuration.JpaTestConfiguration;
import managesys.model.Book;
import managesys.model.Category;
import managesys.model.Format;
import managesys.repository.BookRepository;
import managesys.service.BookService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookService.class)
@ContextConfiguration(classes = JpaTestConfiguration.class)
public class RepositoryTest {

    @Autowired
    private BookRepository repo;

    @Before
    public void setup() {
        Category c = new Category("c1");
        c.save(repo);
        Format f = new Format("f1");
        f.save(repo);

        Book[] books = { new Book("test1", "123-123-123-1", c, f),
                new Book("test2", "223-123-123-1", c, f),
                new Book("test3", "323-123-123-1", c, f),
                new Book("test4", "423-123-123-1", c, f),
                new Book("test5", "523-123-123-1", c, f),
                new Book("test6", "623-123-123-1", c, f),
                new Book("test7", "723-123-123-1", c, f) };
        saveBooks(books);
        repo.flush();
    }

    private void saveBooks(Book... books) {
        for (Book b : books) {
            b.save(repo);
        }
    }

    @Test
    @Transactional
    public void testParameter() {
        List<Book> result = repo.find("from BOOK b where b.title like ?1", Book.class, "%test1%");

        // 期待通りのデータが検出されたことを確認する。
        assertEquals(result.get(0).getTitle(), "test1");
        assertEquals(result.get(0).getIsbn(), "123-123-123-1");
        assertEquals(result.get(0).getCategory().getName(), "c1");
        assertEquals(result.get(0).getFormat().getName(), "f1");
    }

    @Test
    @Transactional
    public void testPaging() {
        Page<Book> result = repo.find("from BOOK b", Book.class, PageRequest.of(0, 5));

        assertEquals(result.getContent().size(), 5);
    }

    @Test
    @Transactional
    public void testSort() {
        Page<Book> result = repo.find("from BOOK b", Book.class, Sort.by(Sort.Direction.DESC, "id"));

        List<Book> list = result.getContent();
        assertTrue(list.get(0).getId() > list.get(list.size() - 1).getId());
    }

    @Test
    @Transactional
    public void testParameterAndPaging() {
        Page<Book> result = repo.find("from BOOK b where b.title like ?1", Book.class, PageRequest.of(0, 5), "%test%");

        assertEquals(result.getContent().size(), 5);
    }

    @Test
    @Transactional
    public void testParameterAndSort() {
        Page<Book> result = repo.find("from BOOK b where b.title like ?1", Book.class,
                Sort.by(Sort.Direction.DESC, "id"), "%test%");

        List<Book> list = result.getContent();
        assertTrue(list.get(0).getId() > list.get(list.size() - 1).getId());
    }

    @Test
    @Transactional
    public void testPagingAndSort() {
        Page<Book> result = repo.find("from BOOK b", Book.class, PageRequest.of(0, 5, Direction.DESC, "id"));

        List<Book> list = result.getContent();
        assertEquals(list.size(), 5);
        assertTrue(list.get(0).getId() > list.get(list.size() - 1).getId());
    }

    @Test
    @Transactional
    public void testParameterAndPagingAndSort() {
        Page<Book> result = repo.find("from BOOK b where b.title like ?1", Book.class,
                PageRequest.of(0, 5, Direction.DESC, "id"), "%test%");

        List<Book> list = result.getContent();
        assertEquals(list.size(), 5);
        assertTrue(list.get(0).getId() > list.get(list.size() - 1).getId());
    }

}
