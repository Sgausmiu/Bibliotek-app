package ru.samara.bibliotek.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.samara.bibliotek.dao.BookDAO;
import ru.samara.bibliotek.dao.PersonDAO;
import ru.samara.bibliotek.models.Book;
import ru.samara.bibliotek.models.Person;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }
    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.show(id));

        Optional<Person> bookOwner = bookDAO.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personDAO.index());
        return "books/show";
    }


    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";

    }
    @PostMapping
    public String create (@ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        bookDAO.save(book);
        return "redirect:/books";
    }
    @GetMapping("/{id}/edit")
    public String edit (Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";

    }
    @PatchMapping("/{id}")
    public String update (@ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult,
                           @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete (@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }
    @PatchMapping("/{id}/tofree")
    public String tofree(@PathVariable("id") int id){
        bookDAO.tofree(id);
        return "redirect:/books/"+ id;
    }
    @PatchMapping("/{id}/toappoint")
    public String toappoint(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson){
        bookDAO.toappoint(id, selectedPerson);
        return "redirect:/books/"+ id;
    }





}

