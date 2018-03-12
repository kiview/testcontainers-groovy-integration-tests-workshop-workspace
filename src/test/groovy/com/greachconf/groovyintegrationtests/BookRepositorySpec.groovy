package com.greachconf.groovyintegrationtests

import com.groovycoder.spockdockerextension.Testcontainers
import spock.lang.Specification

@Testcontainers
class BookRepositorySpec extends Specification {

    BookRepository bookRepository

    def setup() {
        bookRepository = new BookRepository("jdbc:h2:./test",
                "sa",
                "")
        bookRepository.init()
    }

    def cleanup() {
        new File("test.mv.db").delete()
    }

    def "empty repository is empty"() {
        expect:
        bookRepository.count() == 0
    }

    def "repository contains one book after saving it"() {
        given: "the repo"
        def repo = bookRepository

        and: "a book"
        def book = new Book("Moby Dick", "Herman Melville")

        when: "saving it"
        repo.save(book)

        then: "repo contains one book"
        repo.count() == 1
    }

    def "repository finds books of given author"() {
        given: "the repo"
        def repo = bookRepository

        and: "some books"
        def mobyDick = new Book("Moby Dick", "Herman Melville")
        def terryPratchett = "Terry Pratchett"
        def magic = new Book("The Colour of Magic", terryPratchett)
        def elephant = new Book("The Fifth Elephant", terryPratchett)

        when: "saving them"
        [mobyDick, magic, elephant].each { repo.save(it) }

        and: "searching for books of author"
        def foundBooks = repo.findAllByAuthor(terryPratchett)

        then: "expected books were found"
        foundBooks.size() == 2
        foundBooks.containsAll([magic, elephant])
    }


}
