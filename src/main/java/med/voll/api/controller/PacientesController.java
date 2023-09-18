package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacientesController {


    private final PacienteRepository pacienteRepository;

    public PacientesController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping
    public void registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente) {
        pacienteRepository.save(new Paciente(datosRegistroPaciente));

    }

    @GetMapping
    public Page<DatosListadoPaciente> listadoPaciente(@PageableDefault(size = 5, sort = {"nombre"}) Pageable pageable) {

     /*   return pacienteRepository.findAll(pageable).map(DatosListadoPaciente::new);*/
        return pacienteRepository.findByActivoTrue(pageable).map(DatosListadoPaciente::new);
    }


    @PutMapping
    @Transactional
    public void actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente) {
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarDatos(datosActualizarPaciente);

    }


    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarPaciente(@PathVariable long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.desactivarPaciente();
    }


}
