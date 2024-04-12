package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
@Primary
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of("login", user.getLogin(), "name", user.getName(),
                "email", user.getEmail(), "birthday", user.getBirthday().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());

        updFriends(user);
        return getUser(user.getId());
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("update users set login = ?, name  = ?, email  = ?, birthday  = ? " +
                        "where id = ?", user.getLogin(), user.getName(), user.getEmail(),
                user.getBirthday(), user.getId());
        updFriends(user);
        return getUser(user.getId());
    }

    @Override
    public List<User> getUsers() {
        List<User> list = jdbcTemplate.queryForObject("select u.id, u.login, u.name, u.email, u.birthday, f.friend_id" +
                " from users u left join friends f on u.id = f.id order by u.id", listAllMapper());

        return list;
    }


    @Override
    public User getUser(Integer id) {
        User user = jdbcTemplate.queryForObject("select u.id, u.login, u.name, u.email, u.birthday, f.friend_id" +
                " from users u left join friends f on u.id = f.id where u.id = ?", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user1 = createUser(rs);
                do {
                    if (rs.getInt("friend_id") != 0) {
                        user1.getFriendsList().add(rs.getInt("friend_id"));
                    }
                } while (rs.next());

                return user1;
            }
        }, id);
        return user;
    }

    private RowMapper<List<User>> listAllMapper() {
        return (rs, rowNum) -> {
            List<User> usersList = new ArrayList<>();
            int check = -1;
            User user = null;
            do {
                if (rs.getInt("id") != check) {
                    user = createUser(rs);
                    if (rs.getInt("friend_id") != 0) {
                        user.getFriendsList().add(rs.getInt("friend_id"));
                    }
                    usersList.add(user);
                    check = rs.getInt("id");
                } else {
                    if (rs.getInt("friend_id") != 0) {
                        user.getFriendsList().add(rs.getInt("friend_id"));
                    }
                }
            } while (rs.next());
            return usersList;
        };
    }

    private void updFriends(User user) {
        jdbcTemplate.update("DELETE FROM friends WHERE id = ?", user.getId());
        if (!user.getFriendsList().isEmpty()) {
            for (Integer friend : user.getFriendsList()) {
                jdbcTemplate.update("insert into friends (id, friend_id) values (?, ?)", user.getId(), friend);
            }
        }
    }

    private User createUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friendsList(new HashSet<>())
                .build();
    }

}
