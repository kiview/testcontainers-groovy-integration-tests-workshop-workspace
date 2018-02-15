package io.devopgathering.groovyintegrationtests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookRepositoryTests {

    @Test
    public void failingTest() {
        BookRepository bookRepository = new BookRepository(null, null, null);
        assertEquals(0L, bookRepository.count());
    }

}
