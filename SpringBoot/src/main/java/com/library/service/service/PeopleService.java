package com.library.service.service;

import com.library.service.model.Book;
import com.library.service.model.Person;
import com.library.service.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Person findPersonByBooks(List<Book> books) {
        return peopleRepository.findPersonByBooksIn(books);
    }

    public List<Book> getListByPersonId(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        if (foundPerson.isPresent()) {
            Hibernate.initialize(foundPerson.get().getBooks());
            return foundPerson.get().getBooks();
        } else
            return Collections.emptyList();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
