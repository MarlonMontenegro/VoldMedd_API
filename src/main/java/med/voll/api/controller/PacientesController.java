package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacientesController {


    private final PacienteRepository pacienteRepository;

    public PacientesController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaPaciente> registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente,
                                                                    UriComponentsBuilder uriComponentsBuilder) {
        Paciente paciente = pacienteRepository.save(new Paciente(datosRegistroPaciente));
        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getDocumento(), paciente.getTelefono(), new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(),
                paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(),
                paciente.getDireccion().getComplemento()));

        URI url = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaPaciente);

    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listadoPaciente(@PageableDefault(size = 5, sort = {"nombre"}) Pageable pageable) {

        /*   return pacienteRepository.findAll(pageable).map(DatosListadoPaciente::new);*/
        return ResponseEntity.ok(pacienteRepository.findByActivoTrue(pageable).map(DatosListadoPaciente::new));
    }


    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente) {
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarDatos(datosActualizarPaciente);

        return ResponseEntity.ok(new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getDocumento(), paciente.getTelefono(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento())));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarPaciente(@PathVariable long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.desactivarPaciente();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPaciente> retornarDatosPaciente(@PathVariable long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);

        var datosPaciente = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getDocumento(), paciente.getTelefono(),
                new DatosDireccion(paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento()));

        return ResponseEntity.ok(datosPaciente);

    }
}
