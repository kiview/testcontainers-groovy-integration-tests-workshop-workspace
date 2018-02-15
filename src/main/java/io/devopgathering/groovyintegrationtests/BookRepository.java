package io.devopgathering.groovyintegrationtests;

import java.util.List;

public class BookRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public BookRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void save(Book book) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public long count() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public List<Book> findAllByAuthor(String author) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
