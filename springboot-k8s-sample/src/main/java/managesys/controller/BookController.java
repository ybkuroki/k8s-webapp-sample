package managesys.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import managesys.model.Book;
import managesys.model.dto.BookDto.ChgBook;
import managesys.model.dto.BookDto.RegBook;
import managesys.service.BookService;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    BookService service;

    @GetMapping("/list")
    @ResponseBody
    public Page<Book> list(Pageable pageable) {
        return service.findAllBooks(pageable);
    }

    @PostMapping("/new")
    @ResponseBody
    public Book saveBook(@Valid @RequestBody RegBook book) {
        return service.saveBook(book);
    }

    @PostMapping("/edit")
    @ResponseBody
    public Book updateBook(@Valid @RequestBody ChgBook book) {
        return service.updateBook(book);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Book deleteBook(@Valid @RequestBody ChgBook book) {
        return service.deleteBook(book);
    }

    @GetMapping("/search")
    @ResponseBody
    public Page<Book> searchBook(@RequestParam String query, Pageable pageable) {
        return service.findBookByTitle(query, pageable);
    }

    @GetMapping(path = "/allListPdfReport", produces = "application/pdf")
    @ResponseBody
    public ResponseEntity<byte[]> getAllListPdfReport() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline; filename=AllListPdfReport.pdf");
        return new ResponseEntity<byte[]>(service.exportAllListPdfReport(), headers, HttpStatus.OK);
    }
}
