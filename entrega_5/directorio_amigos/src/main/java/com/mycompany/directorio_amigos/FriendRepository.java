package com.mycompany.directorio_amigos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRepository {

    private final Path path;

    public FriendRepository(String rutaArchivo) {
        this.path = Paths.get(rutaArchivo).toAbsolutePath();
    }

    private void asegurarArchivo() throws IOException {
        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }
    }

    private List<Friend> leerTodos() throws IOException {
        asegurarArchivo();
        List<Friend> amigos = new ArrayList<>();

        if (Files.size(path) == 0) {
            return amigos;
        }

        List<String> lineas = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String linea : lineas) {
            if (linea == null || linea.trim().isEmpty()) continue;

            String[] partes = linea.split("!");
            if (partes.length < 2) continue;

            String nombre = partes[0];
            try {
                long numero = Long.parseLong(partes[1]);
                amigos.add(new Friend(nombre, numero));
            } catch (NumberFormatException e) {
                // línea mal formateada, se ignora
            }
        }
        return amigos;
    }

    private void guardarTodos(List<Friend> amigos) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Friend f : amigos) {
            lineas.add(f.getNombre() + "!" + f.getNumero());
        }

        Files.write(
                path,
                lineas,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        );
    }

    // CREATE: devuelve true si se agregó, false si ya existía
    public boolean agregar(Friend nuevo) throws IOException {
        List<Friend> amigos = leerTodos();

        for (Friend f : amigos) {
            if (f.getNombre().equals(nuevo.getNombre())
                    || f.getNumero() == nuevo.getNumero()) {
                return false;
            }
        }

        amigos.add(nuevo);
        guardarTodos(amigos);
        return true;
    }

    public Friend buscarPorNombre(String nombreBuscado) throws IOException {
        List<Friend> amigos = leerTodos();
        for (Friend f : amigos) {
            if (f.getNombre().equals(nombreBuscado)) {
                return f;
            }
        }
        return null;
    }

    public Friend buscarPorNumero(long numeroBuscado) throws IOException {
        List<Friend> amigos = leerTodos();
        for (Friend f : amigos) {
            if (f.getNumero() == numeroBuscado) {
                return f;
            }
        }
        return null;
    }

    public boolean actualizarNumero(String nombreBuscado, long nuevoNumero) throws IOException {
        List<Friend> amigos = leerTodos();
        boolean encontrado = false;

        for (int i = 0; i < amigos.size(); i++) {
            Friend f = amigos.get(i);
            if (f.getNombre().equals(nombreBuscado)) {
                amigos.set(i, new Friend(nombreBuscado, nuevoNumero));
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            guardarTodos(amigos);
        }
        return encontrado;
    }

    public boolean borrarPorNombre(String nombreBuscado) throws IOException {
        List<Friend> amigos = leerTodos();
        int sizeAntes = amigos.size();

        amigos.removeIf(f -> f.getNombre().equals(nombreBuscado));

        if (amigos.size() != sizeAntes) {
            guardarTodos(amigos);
            return true;
        }
        return false;
    }

    public boolean borrarPorNumero(long numeroBuscado) throws IOException {
        List<Friend> amigos = leerTodos();
        int sizeAntes = amigos.size();

        amigos.removeIf(f -> f.getNumero() == numeroBuscado);

        if (amigos.size() != sizeAntes) {
            guardarTodos(amigos);
            return true;
        }
        return false;
    }
}
