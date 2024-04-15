package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.related.UnknownValueException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
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
                "releasedate", film.getReleaseDate().toString(), "duration", film.getDuration().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());

        updMpaAndGenreAndLikeInDataBase(film);
        return getFilm(id.intValue());
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE films SET name = ?, description  = ?, releasedate  = ?, " +
                        "duration = ? WHERE id = ?", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getId());
        updMpaAndGenreAndLikeInDataBase(film);
        return getFilm(film.getId());
    }


    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.queryForObject("SELECT f.id, f.name, f.description, f.releasedate, f.duration, " +
                "l.user_id AS likes, fg.genre_id, g.name AS genre_name, fm.mpa_id, m.name AS mpa_name " +
                "FROM films f " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genre g ON fg.genre_id = g.id " +
                "LEFT JOIN film_mpa fm ON f.id = fm.film_id " +
                "LEFT JOIN mpa m ON fm.mpa_id = m.id " +
                "LEFT JOIN likes l ON f.id = l.film_id ORDER BY f.id", mapperListAllFilms());
    }

    @Override
    public Film getFilm(Integer id) {
        return jdbcTemplate.queryForObject("SELECT f.id, f.name, f.description, f.releasedate, f.duration, " +
                "l.user_id AS likes, fg.genre_id, g.name AS genre_name, fm.mpa_id, m.name AS mpa_name " +
                "FROM films f " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genre g ON fg.genre_id = g.id " +
                "LEFT JOIN film_mpa fm ON f.id = fm.film_id " +
                "LEFT JOIN mpa m ON fm.mpa_id = m.id " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "WHERE f.id = ? " +
                "ORDER BY f.id", this::mapperFilm, id);
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.queryForObject("SELECT g.id AS genre_id, g.name AS genre_name FROM genre g ORDER BY g.id",
                (rs, rowNum) -> {
                    List<Genre> genresList = new ArrayList<>();
                    if (rs.getInt("id") == 0) {
                        return genresList;
                    }
                    do {
                        genresList.add(createGenreBuilder(rs));
                    } while (rs.next());
                    return genresList;
                });
    }

    @Override
    public Genre getGenre(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT g.id AS genre_id, g.name AS genre_name FROM genre g WHERE id = ?",
                    (rs, rowNum) -> createGenreBuilder(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка в запросе к базе данных. Не верный id: {} \n {}", id, e.getMessage());
            throw new UnknownValueException("Не верный id жанра: " + id);
        }
    }

    @Override
    public List<MPA> getMpas() {
        return jdbcTemplate.queryForObject("SELECT m.id AS mpa_id, m.name AS mpa_name FROM mpa m ORDER BY m.id",
                (rs, rowNum) -> {
                    List<MPA> mpaList = new ArrayList<>();
                    if (rs.getInt("id") == 0) {
                        return mpaList;
                    }
                    do {
                        mpaList.add(createMpaBuilder(rs));
                    } while (rs.next());
                    return mpaList;
                });
    }

    @Override
    public MPA getMpa(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT m.id AS mpa_id, m.name AS mpa_name FROM mpa m WHERE id = ?",
                    (rs, rowNum) -> createMpaBuilder(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка в запросе к базе данных. Не верный id: {} \n {}", id, e.getMessage());
            throw new UnknownValueException("Не верный id рейтинга: " + id);
        }
    }

    private Film mapperFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = createFilmBuilder(rs);
        do {
            addLikeAndGenreInFilm(rs, film);
        } while (rs.next());
        return film;
    }

    private RowMapper<List<Film>> mapperListAllFilms() {
        return (rs, rowNum) -> {
            List<Film> filmsList = new ArrayList<>();
            int check = -1;
            Film film = null;
            do {
                if (rs.getInt("id") != check) {
                    film = createFilmBuilder(rs);
                    addLikeAndGenreInFilm(rs, film);

                    filmsList.add(film);
                    check = rs.getInt("id");
                } else {
                    addLikeAndGenreInFilm(rs, film);
                }
            } while (rs.next());
            return filmsList;
        };
    }

    private Film createFilmBuilder(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releasedate").toLocalDate())
                .duration(rs.getInt("duration"))
                .likes(new HashSet<>())
                .genres(new TreeSet<>())
                .build();
    }

    private Genre createGenreBuilder(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    private MPA createMpaBuilder(ResultSet rs) throws SQLException {
        return MPA.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

    private void updMpaAndGenreAndLikeInDataBase(Film film) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?", film.getId());
        if (film.getLikes() != null && !film.getLikes().isEmpty()) {
            for (Integer like : film.getLikes()) {
                jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", film.getId(), like);
            }
        }

        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }

        jdbcTemplate.update("DELETE FROM film_mpa WHERE film_id = ?", film.getId());
        if (film.getMpa() != null) {
            jdbcTemplate.update("INSERT INTO film_mpa (film_id, mpa_id) VALUES (?, ?)",
                    film.getId(), film.getMpa().getId());

        }
    }

    private void addLikeAndGenreInFilm(ResultSet rs, Film film) throws SQLException {
        if (rs.getInt("likes") != 0) {
            film.getLikes().add(rs.getInt("likes"));
        }

        if (rs.getString("genre_name") != null) {
            film.getGenres().add(createGenreBuilder(rs));
        }

        if (film.getMpa() == null && rs.getInt("mpa_id") != 0) {
            film.setMpa(createMpaBuilder(rs));
        }
    }
}

