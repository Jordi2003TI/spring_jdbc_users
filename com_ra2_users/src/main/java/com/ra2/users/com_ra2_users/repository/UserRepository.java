package com.ra2.users.com_ra2_users.repository;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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
     // Encontrar a todos los usarios
     public List<User> findAll(){
        String sql = "SELECT * FROM users;";
        return jdbcTemplate.query(sql, new UserRowMapper());
     }
     // Buscamos por id para que nos encuentre por una id
     public List<User> findOne(long user_id){
        String sql = "SELECT * FROM users WHERE id= ?;";
        return jdbcTemplate.query(sql, new UserRowMapper(), user_id); // ponemos todos los datos que le vamos pasando por parametros
     } 

     //7. Actualizamos un usuario todo completo mediante el put
     public int updateUser(long user_id, User user){
        String sql = "UPDATE users SET nom = ?, descripcion=? ,email = ?, contrasena=? ,dataUpdated = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getNom(), user.getDescripcion(), user.getEmail(), user.getContrasena(), Timestamp.valueOf(LocalDateTime.now()), user_id);
     }


    //Actualizamos un usario mediante patch
    public int updateUserPatch(long user_id, String nombre){
        if(nombre.length() > 100) return 0;
        String sql = "UPDATE users SET nom = ?, dataUpdated = ? WHERE id = ?";
        return jdbcTemplate.update(sql,nombre,Timestamp.valueOf(LocalDateTime.now()),user_id);
    }

    public int deleteUser(long user_id){
        String sql = "DELETE FROM users WHERE id=?";
        return jdbcTemplate.update(sql, user_id);
    }

}
