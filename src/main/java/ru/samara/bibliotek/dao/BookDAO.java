package ru.samara.bibliotek.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.samara.bibliotek.models.Book;
import ru.samara.bibliotek.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Book> index(){
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book(named, author, yearBook) VALUES (?, ?, ?)", book.getNamed(), book.getAuthor(), book.getYearBook());
    }
    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET named=?, author=?, yearBook=? WHERE id=?", updatedBook.getNamed(),
                updatedBook.getAuthor(), updatedBook.getYearBook(), id);
    }
    public void  delete(int id){
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }
    //Для создания выпадающего списка соединим таблицы Book and Person
    public Optional<Person> getBookOwner(int id) {
        //Выбираем все колонки таблицы Person из объединенной таблицы
        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.id " +
                "WHERE Book.id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }
    //Освобожждение  книги читателем
    public void tofree (int id) {
         jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
    }

    //Назначение книги читателю
    public void toappoint (int id, Person selectedPerson) {
         jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", selectedPerson.getId(), id);
    }

}
