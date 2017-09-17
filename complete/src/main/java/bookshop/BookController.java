package bookshop;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final AtomicLong counter = new AtomicLong(3);

    private final List<Book> fullList = new ArrayList<Book>() {
        {
            add(new Book(1, "name 1"));
            add(new Book(2, "name 2"));
            add(new Book(3, "name 3"));
        }
    };

    private List<Book> list = fullList;


    // ----- Get all books -----
    @RequestMapping(value="/books", method=GET)
    public List<Book> books() throws NotFoundException {

        if (list != null) {
            return list;
        } else {
            throw new NoContentException();
        }
    }

    // ----- Get book by id -----
    @RequestMapping(value="/books/{id}", method=GET)
    public Book getBook(@PathVariable("id") long id) {
        for (Book item: list) {
            if (item.getId() == id) {
                return item;
            }
        }

        throw new NoContentException();
    }

    // ----- Create new book -----
    @RequestMapping(value="/books", method=POST)
    @ResponseBody
    public Book createBook(@RequestBody Book book) {

        if (book != null) {
            long newID = counter.incrementAndGet();
            Book newBook = new Book(newID, book.getName());
            list.add(newBook);
            return newBook;
        }

        throw new InternalServerErrorException();
    }

    // ----- Update book by id -----
    @RequestMapping(value="/books/{id}", method=PUT)
    @ResponseBody
    public Book updateBook(@PathVariable("id") long id, @RequestBody Book book) {

        //Find book
        int index = -1;
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                index = i;
            }
        }

        //update book
        if (index >= 0) {
            list.set(index, book);
            return book;
        } else {
            throw new NotFoundException();
        }
    }

    // ----- Delete book by id -----
    @RequestMapping(value="/books/{id}", method=DELETE)
    public void updateBook(@PathVariable("id") long id) {

        //Find book
        int index = -1;
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                index = i;
            }
        }

        //update book
        if (index >= 0) {
            list.remove(index);
        } else {
            throw new NotFoundException();
        }
    }

    //404
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class NotFoundException extends RuntimeException {
    }

    //204
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public class NoContentException extends RuntimeException {
    }

    //500
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public class InternalServerErrorException extends RuntimeException {
    }

}
