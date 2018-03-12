package com.greachconf.groovyintegrationtests;

import org.flywaydb.core.Flyway;

import java.sql.*;
import java.util.LinkedList;
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

    public void init() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(jdbcUrl, username, password);
        flyway.migrate();
    }

    public void save(Book book) {
        try (Connection c = DriverManager.getConnection(jdbcUrl, username, password)) {
            String insertBookSql = "INSERT INTO books (name, author) VALUES (?, ?)";

            PreparedStatement ps = c.prepareStatement(insertBookSql);
            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long count() {
        try (Connection c = DriverManager.getConnection(jdbcUrl, username, password)) {

            c.createStatement().execute("SET @foobar = 4");

            ResultSet rs = c.createStatement().executeQuery("SELECT COUNT (*) FROM books");
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findAllByAuthor(String author) {
        try (Connection c = DriverManager.getConnection(jdbcUrl, username, password)) {

            String selectSql = "SELECT name FROM books WHERE author = ?";
            PreparedStatement ps = c.prepareStatement(selectSql);
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
