package managesys.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import managesys.model.Book;
import managesys.model.Category;
import managesys.model.Format;

public class BookDto {
    // 登録
    public static class RegBook {
        @Size(min = 3, max = 50, message="{Size.book.title}")
        private String title;
        @Size(min = 10, max = 20, message="{Size.book.isbn}")
        private String isbn;
        @NotNull
        private int categoryId = 0;
        @NotNull
        private int formatId = 0;

        public Book create() {
            Category c = new Category(categoryId, "");
            Format f = new Format(formatId, "");
            return new Book(title, isbn, c, f);
        }

        public RegBook() {}

        public RegBook(String title, String isbn, int categoryId, int formatId) {
            super();
            this.title = title;
            this.isbn = isbn;
            this.categoryId = categoryId;
            this.formatId = formatId;
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
        public int getCategoryId() {
            return categoryId;
        }
        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
        public int getFormatId() {
            return formatId;
        }
        public void setFormatId(int formatId) {
            this.formatId = formatId;
        }
    }

    // 更新、削除
    public static class ChgBook {
        @NotNull
        private int id = 0;
        @Size(min = 3, max = 50, message="{Size.book.title}")
        private String title;
        @Size(min = 10, max = 20, message="{Size.book.isbn}")
        private String isbn;
        @NotNull
        private int categoryId = 0;
        @NotNull
        private int formatId = 0;

        public ChgBook() {}

        public ChgBook(int id, String title, String isbn, int categoryId, int formatId) {
            super();
            this.id = id;
            this.title = title;
            this.isbn = isbn;
            this.categoryId = categoryId;
            this.formatId = formatId;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
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
        public int getCategoryId() {
            return categoryId;
        }
        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
        public int getFormatId() {
            return formatId;
        }
        public void setFormatId(int formatId) {
            this.formatId = formatId;
        }
    }
}
