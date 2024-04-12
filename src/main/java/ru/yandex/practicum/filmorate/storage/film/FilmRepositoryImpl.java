package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.related.FilmGenre;
import ru.yandex.practicum.filmorate.related.UnknownValueException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
@Primary
@Slf4j
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of("name", film.getName(), "description", film.getDescription(),
                "rating", film.getRating(), "releasedate", film.getReleaseDate().toString(),
                "duration", film.getDuration().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());

        updGenreAndLike(film);

        return getFilm(id.intValue());
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("update films set name = ?, description  = ?, rating  = ?, releasedate  = ?, " +
                        "duration = ? where id = ?", film.getName(), film.getDescription(), film.getRating(),
                film.getReleaseDate(), film.getDuration(), film.getId());
        updGenreAndLike(film);
        return getFilm(film.getId());
    }


    @Override
    public List<Film> getFilms() {
        try {
            return jdbcTemplate.queryForObject("SELECT f.id, f.name, f.description, g.genre, " +
                    "f.rating, f.releasedate, f.duration, l.user_id as likes " +
                    "FROM films f " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.genre_id " +
                    "LEFT JOIN likes l ON f.id = l.film_id " +
                    "ORDER BY f.id DESC", listAllMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка в запросе к базе данных. \n {}", e.getMessage());
            throw new UnknownValueException("Не найдено значений для возврата.");
        }
    }

    @Override
    public Film getFilm(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT f.id, f.name, f.description, g.genre, " +
                    "f.rating, f.releasedate, f.duration, l.user_id AS likes " +
                    "FROM films f " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genre g ON fg.genre_id = g.genre_id " +
                    "LEFT JOIN likes l ON f.id = l.film_id " +
                    "WHERE f.id = ?", this::filmMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка в запросе к базе данных. Не найдено значение по id: {} \n {}", id, e.getMessage());
            throw new UnknownValueException("Не верный id: " + id + " фильма");
        }
    }

    private Film filmMapper(ResultSet rs, int rowNum) throws SQLException {
        Film film = createFilm(rs);
        do {
            addLikeAndGenre(rs, film);
        } while (rs.next());
        return film;
    }

    private RowMapper<List<Film>> listAllMapper() {
        return (rs, rowNum) -> {
            List<Film> filmsList = new ArrayList<>();
            int check = -1;
            Film film = null;
            do {
                if (rs.getInt("id") != check) {
                    film = createFilm(rs);
                    addLikeAndGenre(rs, film);

                    filmsList.add(film);
                    check = rs.getInt("id");
                } else {
                    addLikeAndGenre(rs, film);
                }
            } while (rs.next());
            return filmsList;
        };
    }

    private Film createFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .rating(rs.getString("rating"))
                .releaseDate(rs.getDate("releasedate").toLocalDate())
                .duration(rs.getInt("duration"))
                .likes(new HashSet<>())
                .genre(new HashSet<>())
                .build();
    }

    private int caseGenre(FilmGenre genre) {
        switch (genre) {
            case COMEDY:
                return 1;
            case DRAMA:
                return 2;
            case CARTOON:
                return 3;
            case THRILLER:
                return 4;
            case DOCUMENTARY:
                return 5;
            case BLOCKBUSTER:
                return 6;
        }
        return -1;
    }

    private void updGenreAndLike(Film film) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?", film.getId());
        if (!film.getLikes().isEmpty()) {
            for (Integer like : film.getLikes()) {
                jdbcTemplate.update("insert into likes (film_id, user_id) values (?, ?)", film.getId(), like);
            }
        }

        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
        if (!film.getGenre().isEmpty()) {
            for (FilmGenre genre : film.getGenre()) {
                jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)",
                        film.getId(), caseGenre(genre));
            }
        }
    }

    private void addLikeAndGenre(ResultSet rs, Film film) throws SQLException {
        if (rs.getInt("likes") != 0) {
            film.getLikes().add(rs.getInt("likes"));
        }
        if (rs.getString("genre") != null) {
            film.getGenre().add(FilmGenre.valueOf(rs.getString("genre")));
        }
    }
}

