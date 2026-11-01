package foodloop_api.controllers;
import foodloop_api.dto.*;
import foodloop_api.models.*;
import foodloop_api.services.FoodLoopService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/foodloop") 
public class FoodLoopController {
 private final FoodLoopService f;
 private String usuario(Authentication a) { return a==null?null:a.getName(); }
 @GetMapping("/lotes") public List<Map<String,Object>> catalogo(){return f.catalogo();}
 @GetMapping("/lotes/{id}") public Map<String,Object> detalle(@PathVariable Long id){return f.detalle(id);}
 @GetMapping("/mi/comercios") public List<Establecimiento> misComercios(Authentication a){return f.misComercios(usuario(a));}
 @PostMapping("/comercios") public Establecimiento comercio(Authentication a,@RequestBody EstablecimientoDTO d){return f.registrarComercio(usuario(a),d);}
 @GetMapping("/mi/lotes") public List<Map<String,Object>> misLotes(Authentication a){return f.misLotes(usuario(a));}
 @PostMapping("/lotes") public Map<String,Object> publicar(Authentication a,@RequestBody PublicarLoteDTO d){return f.publicar(usuario(a),d);}
 @PatchMapping("/lotes/{id}/estado") public Map<String,Object> estado(Authentication a,@PathVariable Long id,@RequestParam String valor){return f.pausarLote(usuario(a),id,valor);}
 @PostMapping("/organizaciones/perfil") public OrganizacionBeneficiaria org(Authentication a,@RequestBody OrganizacionDTO d){return f.guardarOrganizacion(usuario(a),d);}
 @GetMapping("/organizaciones/mi-perfil") public Optional<OrganizacionBeneficiaria> perfil(Authentication a){return f.miOrganizacion(usuario(a));}
 @GetMapping("/admin/organizaciones") public List<OrganizacionBeneficiaria> orgPendientes(Authentication a){return f.organizacionesPendientes(usuario(a));}
 @PostMapping("/admin/organizaciones/{id}/verificar") public Map<String,String> verificar(Authentication a,@PathVariable Long id){f.verificarOrganizacion(usuario(a),id);return Map.of("estado","VERIFICADA");}
 @PostMapping("/solicitudes") @ResponseStatus(HttpStatus.CREATED) public Solicitud pedir(Authentication a,@RequestBody SolicitudFoodDTO d){return f.pedir(usuario(a),d);}
 @GetMapping("/mi/solicitudes") public List<Solicitud> misSolicitudes(Authentication a){return f.misSolicitudes(usuario(a));}
 @GetMapping("/comercio/solicitudes") public List<Solicitud> solicitudesComercio(Authentication a){return f.solicitudesComercio(usuario(a));}
 @PostMapping("/solicitudes/{id}/asignar") public Solicitud asignar(Authentication a,@PathVariable Long id,@RequestParam int cantidad){return f.asignar(usuario(a),id,cantidad);}
 @PostMapping("/solicitudes/{id}/aceptar-parcial") public Solicitud aceptar(Authentication a,@PathVariable Long id,@RequestParam boolean aceptar){return f.aceptarParcial(usuario(a),id,aceptar);}
 @PostMapping("/solicitudes/{id}/cancelar") public Solicitud cancelar(Authentication a,@PathVariable Long id){return f.cancelar(usuario(a),id);}
 @PostMapping("/solicitudes/{id}/lista") public Solicitud lista(Authentication a,@PathVariable Long id){return f.marcarLista(usuario(a),id);}
 @PostMapping("/solicitudes/{id}/entregar") public Solicitud entregar(Authentication a,@PathVariable Long id,@RequestParam String codigo){return f.entregar(usuario(a),id,codigo);}
 @GetMapping("/solicitudes/{id}/entrega") public Entrega entrega(Authentication a,@PathVariable Long id){return f.entrega(usuario(a),id);}
 @GetMapping("/mi/notificaciones") public List<Notificacion> notificaciones(Authentication a){return f.misAvisos(usuario(a));}
 @PostMapping("/incidencias") public Incidencia incidencia(Authentication a,@RequestBody IncidenciaDTO d){return f.incidencia(usuario(a),d);}
 @GetMapping("/mi/incidencias") public List<Incidencia> incidencias(Authentication a){return f.misIncidencias(usuario(a));}
 @GetMapping("/admin/incidencias") public List<Incidencia> todasIncidencias(Authentication a){return f.todasIncidencias(usuario(a));}
 @PostMapping("/admin/incidencias/{id}/resolver") public Incidencia resolver(Authentication a,@PathVariable Long id,@RequestParam String nota){return f.resolverIncidencia(usuario(a),id,nota);}
 @PostMapping("/lotes/{id}/conservacion") public HistorialConservacion conservacion(Authentication a,@PathVariable Long id,@RequestParam(required=false) Double temperatura,@RequestParam String observacion){return f.registrarConservacion(usuario(a),id,temperatura,observacion);}
 @GetMapping("/lotes/{id}/conservacion") public List<HistorialConservacion> historial(Authentication a,@PathVariable Long id){return f.historialConservacion(usuario(a),id);}
 @GetMapping("/comercio/metricas") public Map<String,Object> metricas(Authentication a){return f.metricas(usuario(a));}
 @GetMapping("/comercio/estimacion") public Map<String,Object> estimacion(Authentication a){return f.estimacion(usuario(a));}

    // Metodos escritos de forma explicita, sin Lombok.
    public FoodLoopController(FoodLoopService f) {
        this.f = f;
    }

}
