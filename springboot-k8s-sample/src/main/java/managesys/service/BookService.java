package managesys.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import managesys.model.Book;
import managesys.model.Category;
import managesys.model.Format;
import managesys.model.dto.BookDto.ChgBook;
import managesys.model.dto.BookDto.RegBook;
import managesys.report.AllListPdfReporter;
import managesys.repository.BookRepository;

@Service("bookLogic")
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    protected ResourceLoader resourceLoader;

    @Transactional
    public Book saveBook(RegBook book) {
        Optional<Category> category = Category.findById(bookRepository, book.getCategoryId());
        Optional<Format> format = Format.findById(bookRepository, book.getFormatId());

        Book entity = book.create();
        if (category.isPresent() && format.isPresent()) {
            entity.setCategory(category.get());
            entity.setFormat(format.get());
            entity.save(bookRepository);
            return entity;
        }
        return null;
    }

    @Transactional
    public Book updateBook(ChgBook book) {
        Optional<Book> optBook = Book.findById(bookRepository, book.getId());

        if (optBook.isPresent()) {
            Book entity = optBook.get();
            entity.setTitle(book.getTitle());
            entity.setIsbn(book.getIsbn());

            Optional<Category> category = Category.findById(bookRepository, book.getCategoryId());
            Optional<Format> format = Format.findById(bookRepository, book.getFormatId());

            if (category.isPresent() && format.isPresent()) {
                entity.setCategory(category.get());
                entity.setFormat(format.get());
                entity.save(bookRepository);
            }
            return entity;
        }
        return null;
    }

    @Transactional
    public Book deleteBook(ChgBook book) {
        Optional<Book> optBook = Book.findById(bookRepository, book.getId());
        if (optBook.isPresent()) {
            Book entity = optBook.get();
            entity.delete(bookRepository);
            return entity;
        }
        return null;
    }

    @Transactional
    public Page<Book> findAllBooks(Pageable pageable) {
        return Book.findAll(bookRepository, pageable);
    }

    @Transactional
    public Book findBookByIsbn(String isbn) {
        Optional<Book> optBook = Book.findByIsbn(bookRepository, isbn);
        if (optBook.isPresent()) {
            return optBook.get();
        }
        return null;
    }

    @Transactional
    public Page<Book> findBookByTitle(String keyword, Pageable pageable) {
        return Book.findByTitle(bookRepository, keyword, pageable);
    }

    @Transactional
    public byte[] exportAllListPdfReport() throws IOException {
        AllListPdfReporter builder = new AllListPdfReporter();
        return builder.makeReport(Book.findAll(bookRepository), resourceLoader.getResource("classpath:ipag.ttf").getURL().openStream());
    }
}
