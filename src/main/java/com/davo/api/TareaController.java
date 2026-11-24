package com.davo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);

    public TareaController() {
        tareas.add(new Tarea(contadorId.getAndIncrement(), "Configurar Maven y Spring Boot", true));
        tareas.add(new Tarea(contadorId.getAndIncrement(), "Crear primer endpoint REST", false));
    }

    // GET http://localhost:8080/api/tareas
    @GetMapping
    public List<Tarea> listarTareas() {
        return tareas;
    }

    // GET http://localhost:8080/api/tareas/1
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        return tareas.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:8080/api/tareas
    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@RequestBody Tarea nuevaTarea) {
        nuevaTarea.setId(contadorId.getAndIncrement());
        tareas.add(nuevaTarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
    }

    // DELETE http://localhost:8080/api/tareas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        boolean eliminada = tareas.removeIf(t -> t.getId().equals(id));
        if (eliminada) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}