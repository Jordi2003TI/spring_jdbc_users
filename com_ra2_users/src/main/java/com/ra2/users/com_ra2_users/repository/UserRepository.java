package com.ra2.users.com_ra2_users.repository;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra2.users.com_ra2_users.model.User;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User>{
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException{
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setNom(rs.getString("nom"));
            user.setDescripcion(rs.getString("descripcion"));
            user.setEmail(rs.getString("email"));
            user.setContrasena(rs.getString("contrasena"));

            if (rs.getTimestamp("ultimAcces") != null) {
                user.setUltimAcess(rs.getTimestamp("ultimAcces").toLocalDateTime());
            }
            if (rs.getTimestamp("dataCreated") != null) {
                user.setDataCreated(rs.getTimestamp("dataCreated").toLocalDateTime());
            }
            if (rs.getTimestamp("dataUpdated") != null) {
                user.setDataUpdate(rs.getTimestamp("dataUpdated").toLocalDateTime());
            }

            return user;
        }
    }

     public int insertUser(User user){
        String sql = """
                INSERT INTO users (nom, descripcion, email, contrasena, ultimAcces, dataCreated, dataUpdated)
                VALUES (?,?,?,?,?,?,?);
                """;
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);

                return jdbcTemplate.update(sql,
                user.getNom(),
                user.getDescripcion(),
                user.getEmail(),
                user.getContrasena(),
                null,              // ultimAcces → null al crear
                timestamp,         // dataCreated → ahora
                timestamp
            );

     }
}
