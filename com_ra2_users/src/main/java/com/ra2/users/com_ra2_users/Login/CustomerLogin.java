package com.ra2.users.com_ra2_users.Login;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component // lo que no sea service, repositori, model o controler ponemos Component
public class CustomerLogin {

    private final String logDirectory = "logs/";

    public CustomerLogin() {

    }
    
    public void error(String clase, String metodo, String resultado) throws IOException{
        // Escribir en el fichero un error
        LocalDateTime hoy = LocalDateTime.now();

        int year = hoy.getYear();
        int mes = hoy.getMonthValue();
        int day = hoy.getDayOfMonth();

        String fechaHoy = "" + year + "-" + mes + "-" + day;

        Files.createDirectory(Paths.get(logDirectory));

        Path path = Paths.get(logDirectory+"aplicacio-"+year+"-"+mes+"-"+day+".log");

        try(var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)){
            writer.write("[" + fechaHoy + "] ERROR - " + clase + " - " + metodo + " - " + resultado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void info(String clase, String metodo, String resultado) throws IOException{
        // Escribir en el fichero un info
        // Escribir en el fichero un error
        LocalDateTime hoy = LocalDateTime.now();

        int year = hoy.getYear();
        int mes = hoy.getMonthValue();
        int day = hoy.getDayOfMonth();

        String fechaHoy = "" + year + "-" + mes + "-" + day;

        Files.createDirectories(Paths.get(logDirectory));

        Path path = Paths.get(logDirectory+"aplicacio-"+year+"-"+mes+"-"+day+".log");

        try(var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)){
            writer.write("[" + fechaHoy + "] INFO - " + clase + " - " + metodo + " - " + resultado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    

}
