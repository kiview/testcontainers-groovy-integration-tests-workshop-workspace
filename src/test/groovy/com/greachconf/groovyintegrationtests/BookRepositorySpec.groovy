package com.greachconf.groovyintegrationtests

import org.junit.After
import org.junit.Before
import org.junit.Test

class BookRepositorySpec {

    BookRepository bookRepository

    @Before
    void setup() {
        bookRepository = new BookRepository("jdbc:h2:./test",
                "sa",
                "")
        bookRepository.init()
    }

    @After
    void cleanup() {
        bookRepository.close()
        new File("test.mv.db").delete()
    }

    @Test
    void "empty repository is empty"() {
        assert bookRepository.count() == 0
    }

    @Test
    void "repository contains one book after saving it"() {
        def repo = bookRepository
        def book = new Book("Moby Dick", "Herman Melville")

        repo.save(book)

        assert repo.count() == 1
    }

    @Test
    void "repository contains another book after saving it"() {
        def repo = bookRepository
        def book = new Book("The Colour of Magic", "Terry Pratchett")

        repo.save(book)

        assert repo.count() == 1
    }

    @Test
    void "repository finds books of given author"() {
        given: "the repo"
        def repo = bookRepository

        def mobyDick = new Book("Moby Dick", "Herman Melville")
        def terryPratchett = "Terry Pratchett"
        def magic = new Book("The Colour of Magic", terryPratchett)
        def elephant = new Book("The Fifth Elephant", terryPratchett)

        [mobyDick, magic, elephant].each { repo.save(it) }
        def foundBooks = repo.findAllByAuthor(terryPratchett)

        assert foundBooks.size() == 2
        assert foundBooks.containsAll([magic, elephant])
    }


}
