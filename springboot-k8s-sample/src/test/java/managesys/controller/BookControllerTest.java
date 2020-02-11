package managesys.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import managesys.RequestUrl;
import managesys.UinitTestSupport;
import managesys.WebTestSupport;
import managesys.model.Book;
import managesys.model.dto.BookDto;
import managesys.service.BookService;

@RunWith(SpringRunner.class)
public class BookControllerTest extends WebTestSupport {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    private List<Book> bookList;

    @Before
    public void setup() throws IOException {
        bookList = UinitTestSupport.generateBooksData();
    }

    @Test
    public void listBooks() throws JsonProcessingException {
        Page<Book> page = UinitTestSupport.createPage(bookList.subList(0, 5), 0, 5, 2);
        when(bookService.findAllBooks(any())).thenReturn(page);

        String url = RequestUrl.Builder.Builder().url("/api/book/list")
                                .param("page", "0")
                                .param("size", "5")
                                .build().getRequestUrl();

        ResponseEntity<String> res = get(url, String.class);

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(res.getBody(), mapper.writeValueAsString(page));
    }

    @Test
    public void saveBookWithSuccess() throws IOException {
        Book book = bookList.get(0);
        when(bookService.saveBook(any())).thenReturn(book);

        ResponseEntity<Object> res = post("/api/book/new", mapper.writeValueAsString(book));

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(JsonPath.read(res.getBody(), "$['title']"), "Test_0");
        assertEquals(JsonPath.read(res.getBody(), "$['isbn']"), "123-234-567-0");

        verify(bookService, atLeastOnce()).saveBook(any(BookDto.RegBook.class));
    }

    @Test
    public void saveBookWithFailure() throws JsonProcessingException {
        Book book = UinitTestSupport.generateValidBookData();
        when(bookService.saveBook(any())).thenReturn(book);

        ResponseEntity<Object> res = post("/api/book/new", mapper.writeValueAsString(book));

        assertThat(res.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(JsonPath.read(res.getBody(), "$['title']"), "書籍タイトルは3文字以上，50文字以下で入力してください");
        assertEquals(JsonPath.read(res.getBody(), "$['isbn']"), "ISBNは10文字以上，20文字以下で入力してください");

        verify(bookService, never()).saveBook(any(BookDto.RegBook.class));
    }

    @Test
    public void updateBookWithSuccess() throws JsonProcessingException {
        Book book = bookList.get(0);
        when(bookService.updateBook(any())).thenReturn(book);

        ResponseEntity<Object> res = post("/api/book/edit", mapper.writeValueAsString(book));

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(JsonPath.read(res.getBody(), "$['title']"), "Test_0");
        assertEquals(JsonPath.read(res.getBody(), "$['isbn']"), "123-234-567-0");

        verify(bookService, atLeastOnce()).updateBook(any(BookDto.ChgBook.class));
    }

    @Test
    public void updateBookWithFailure() throws JsonProcessingException {
        Book book = UinitTestSupport.generateValidBookData();
        when(bookService.updateBook(any())).thenReturn(book);

        ResponseEntity<Object> res = post("/api/book/edit", mapper.writeValueAsString(book));

        assertThat(res.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(JsonPath.read(res.getBody(), "$['title']"), "書籍タイトルは3文字以上，50文字以下で入力してください");
        assertEquals(JsonPath.read(res.getBody(), "$['isbn']"), "ISBNは10文字以上，20文字以下で入力してください");

        verify(bookService, never()).updateBook(any(BookDto.ChgBook.class));
    }

    @Test
    public void searchBook() throws Exception {
        List<Book> contents = new ArrayList<Book>(1);
        contents.add(bookList.get(0));
        Page<Book> page = UinitTestSupport.createPage(contents, 0, 5, 1);

        when(bookService.findBookByTitle(anyString(), any())).thenReturn(page);

        String url = RequestUrl.Builder.Builder().url("/api/book/search")
                                .param("query", bookList.get(0).getTitle())
                                .param("page", "0")
                                .param("size", "5")
                                .build().getRequestUrl();

        ResponseEntity<String> res = get(url, String.class);

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(res.getBody(), mapper.writeValueAsString(page));
    }

    @Test
    public void deleteBook() throws JsonProcessingException {
        Book book = bookList.get(2);
        when(bookService.deleteBook(any())).thenReturn(book);

        ResponseEntity<Object> res = post("/api/book/delete", mapper.writeValueAsString(book));

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(res.getHeaders().get("Content-Type").get(0), is("application/json"));
        assertEquals(JsonPath.read(res.getBody(), "$['title']"), "Test_2");
        assertEquals(JsonPath.read(res.getBody(), "$['isbn']"), "123-234-567-2");

        verify(bookService, atLeastOnce()).deleteBook(any(BookDto.ChgBook.class));
    }
}
