package com.ra2.users.com_ra2_users.model;

import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowCallbackHandler;

public class User {
    private Long id;
    private String nom;
    private String descripcion;
    private String email;
    private String contrasena;
    private String pathruta;
    private LocalDateTime ultimAcess;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdate;
    
    public User() {
    }

    public User(Long id, String nom, String descripcion, String email, String contrasena, String pathruta ,LocalDateTime ultimAcess,
            LocalDateTime dataCreated, LocalDateTime dataUpdate) {
        this.id = id;
        this.nom = nom;
        this.descripcion = descripcion;
        this.email = email;
        this.contrasena = contrasena;
        this.pathruta = pathruta;
        this.ultimAcess = ultimAcess;
        this.dataCreated = dataCreated;
        this.dataUpdate = dataUpdate;
    }

    public User(String nom, String descripcion, String email, String contrasena){
        this.nom = nom;
        this.descripcion = descripcion;
        this.email = email;
        this.contrasena = contrasena;
        this.pathruta = null;
        this.ultimAcess = null;
        this.dataCreated = null;
        this.dataUpdate = null;

    }
    public User(String nom, String descripcion, String email, String contrasena, LocalDateTime creado, LocalDateTime update){
        this.nom = nom;
        this.descripcion = descripcion;
        this.email = email;
        this.contrasena = contrasena;
        this.pathruta = null;
        this.ultimAcess = null;
        this.dataCreated = null;
        this.dataUpdate = null;

    }
    public Long getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getEmail() {
        return email;
    }
    public String getContrasena() {
        return contrasena;
    }
    public LocalDateTime getUltimAcess() {
        return ultimAcess;
    }
    public LocalDateTime getDataCreated() {
        return dataCreated;
    }
    public LocalDateTime getDataUpdate() {
        return dataUpdate;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public void setUltimAcess(LocalDateTime ultimAcess) {
        this.ultimAcess = ultimAcess;
    }
    public void setDataCreated(LocalDateTime dataCreated) {
        this.dataCreated = dataCreated;
    }
    public void setDataUpdate(LocalDateTime dataUpdate) {
        this.dataUpdate = dataUpdate;
    }
    public String getImage_Path() {
        return pathruta;
    }

    public void setImage_Path(String pathruta) {
        this.pathruta = pathruta;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", nom=" + nom + ", descripcion=" + descripcion + ", email=" + email + ", contrasena="
                + contrasena + ", pathruta=" + pathruta + ", ultimAcess=" + ultimAcess + ", dataCreated="
                + dataCreated + ", dataUpdate=" + dataUpdate + "]";
    }

    

    

}
