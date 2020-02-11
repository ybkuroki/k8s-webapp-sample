package managesys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import managesys.model.Book;
import managesys.model.Category;
import managesys.model.Format;

public class UinitTestSupport {

    private static final int SIZE = 7;

    public static List<Book> generateBooksData() {
        List<Book> bookList = new ArrayList<Book>(SIZE);
        List<Category> categoryList = generateCategoriesData();
        List<Format> formatList = generateFormatsData();

        for (int i = 0; i < SIZE; i++) {
            Category c = categoryList.get((i + 1) % 3);
            Format f = formatList.get((i + 1) % 2);
            bookList.add(new Book("Test_" + i, "123-234-567-" + i, c, f));
        }

        return bookList;
    }

    public static Book generateValidBookData() {
        Category c = new Category("技術書");
        c.setId(1);
        Format f = new Format("書籍");
        f.setId(1);
        return new Book("AB", "144", c, f);
    }

    public static List<Category> generateCategoriesData() {
        List<Category> categoryList = new ArrayList<Category>(3);
        categoryList.add(new Category("技術書"));
        categoryList.add(new Category("小説"));
        categoryList.add(new Category("雑誌"));

        return categoryList;
    }

    public static List<Format> generateFormatsData() {
        List<Format> formatList = new ArrayList<Format>(2);
        formatList.add(new Format("書籍"));
        formatList.add(new Format("電子書籍"));

        return formatList;
    }

    public static Page<Book> createPage(List<Book> contents, int page, int size, long totalPages) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<Book>(contents, pageable, totalPages);
    }
}
