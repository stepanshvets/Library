package com.library.service.service;

import com.library.service.model.Book;
import com.library.service.model.Person;
import com.library.service.repositories.BooksRepository;
import com.library.service.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int size) {
        return booksRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public List<Book> findAll(Sort var) {
        return booksRepository.findAll(var);
    }

    public List<Book> findAll(Integer page, Integer size, Sort var) {
        return booksRepository.findAll(PageRequest.of(page, size, var)).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public List<Book> findBookByNameStartingWith(String name) {
        return booksRepository.findBookByNameStartingWithIgnoreCase(name);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book book = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setPerson(book.getPerson());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()){
            book.get().setPerson(null);
            book.get().setStart(null);
            update(id, book.get());
        }
    }

    @Transactional
    public void assign(int bookId, int personId) {
        Optional<Book> book = booksRepository.findById(bookId);
        Optional<Person> person =  peopleRepository.findById(personId);
        if (book.isPresent() && person.isPresent()){
            book.get().setPerson(person.get());
            book.get().setStart(new Date());
            update(bookId, book.get());
        }
    }
}
