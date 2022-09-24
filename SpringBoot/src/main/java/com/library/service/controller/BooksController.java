package com.library.service.controller;

import com.library.service.model.Book;
import com.library.service.model.Person;
import com.library.service.service.BooksService;
import com.library.service.service.PeopleService;
import com.library.service.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService, BookValidator bookValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.bookValidator = bookValidator;
    }


    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer size,
                        @RequestParam(value = "sort_by_year", defaultValue = "false", required = false) Boolean sort,
                        Model model) {
        if (page != null && size != null && sort)
            model.addAttribute("books", booksService.findAll(page, size, Sort.by("year")));
        else if (page != null && size != null)
            model.addAttribute("books", booksService.findAll(page, size));
        else if (sort)
            model.addAttribute("books", booksService.findAll(Sort.by("year")));
        else
            model.addAttribute("books", booksService.findAll());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Person owner = booksService.findOne(id).getPerson();
        if (owner != null)
            model.addAttribute("owner", owner);
        else
            model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@ModelAttribute("person") Person person,
                         @PathVariable("id") int id) {
        booksService.assign(id, person.getId());
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "name", required = false) String name,
                         Model model) {
        if (name != null) {
            System.out.println(name);
            model.addAttribute("books", booksService.findBookByNameStartingWith(name));
        }
        return "books/search";
    }
}
