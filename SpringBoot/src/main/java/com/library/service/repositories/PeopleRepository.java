package com.library.service.repositories;

import com.library.service.model.Book;
import com.library.service.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Person findPersonByBooksIn(List<Book> books);
}
