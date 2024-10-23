package com.estsoft.springproject.bookproject.controller;

import com.estsoft.springproject.bookproject.domain.Book;
import com.estsoft.springproject.bookproject.domain.BookDTO;
import com.estsoft.springproject.bookproject.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/books")
@Controller
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }


    @PostMapping
    public String addBook(@RequestParam String id,
                      @RequestParam String name,
                      @RequestParam String author) {
        service.save(new Book(id, name, author));

        return "redirect:/books";
    }

    @GetMapping
    public String showAll(Model model) {
        List<BookDTO> bookDTOList = service.findAll().stream()
                .map(Book::convert)
                .toList();

        model.addAttribute("bookList", bookDTOList);

        return "bookManagement";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable String id, Model model) {
        Book book = service.findById(id);
        model.addAttribute("book", new BookDTO(book));

        return "bookList";
    }

}
