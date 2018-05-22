package com.greachconf.groovyintegrationtests;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BookRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private Connection connection;

    public BookRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void init() {

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            String createSql = "CREATE TABLE books (\n" +
                    "  id SERIAL,\n" +
                    "  name varchar(255),\n" +
                    "  author varchar(255)\n" +
                    ");";

            connection.createStatement().execute(createSql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Book book) {
        try {
            String insertBookSql = "INSERT INTO books (name, author) VALUES (?, ?)";

            PreparedStatement ps = connection.prepareStatement(insertBookSql);
            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long count() {
        try {

            connection.createStatement().execute("SET @foobar = 4");

            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT (*) FROM books");
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findAllByAuthor(String author) {
        try {

            String selectSql = "SELECT name FROM books WHERE author = ?";
            PreparedStatement ps = connection.prepareStatement(selectSql);
            ps.setString(1, author);

            ResultSet rs = ps.executeQuery();

            List<Book> books = new LinkedList<>();
            while (rs.next()) {
                String name = rs.getString(1);
                books.add(new Book(name, author));
            }

            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
