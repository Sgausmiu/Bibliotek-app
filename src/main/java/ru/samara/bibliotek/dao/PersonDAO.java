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
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
            }
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }
    public void save(Person person){
        jdbcTemplate.update("INSERT INTO Person(fullName, yearBirth) VALUES(?, ?)", person.getFullName(),
                person.getYearBirth());
    }
    public void update(int id, Person updatedPerson){
    jdbcTemplate.update("UPDATE Person SET fullName=?, yearBirth=? WHERE id=?", updatedPerson.getFullName(),
        updatedPerson.getYearBirth(), id);
    }
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }
    //Валидация уникального ФИО
    public Optional<Person> getPersonByFullName(String fullName){
        return jdbcTemplate.query("SELECT * FROM Person WHERE fullName=?", new Object[]{fullName},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
    //Получение человеком книги по его id, без join
    public List<Book> getBooksByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
