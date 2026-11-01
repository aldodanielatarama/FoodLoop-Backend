package foodloop_api.services;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import foodloop_api.models.Establecimiento;
import foodloop_api.models.LoteExcedente;
import foodloop_api.models.OrganizacionBeneficiaria;
import foodloop_api.models.Producto;
import foodloop_api.models.Usuario;
import foodloop_api.repositories.EstablecimientoRepository;
import foodloop_api.repositories.LoteExcedenteRepository;
import foodloop_api.repositories.OrganizacionBeneficiariaRepository;
import foodloop_api.repositories.ProductoRepository;
import foodloop_api.repositories.UsuarioRepository;

@Component @Profile("demo") 
public class DatosDemo implements CommandLineRunner {
 private final UsuarioRepository usuarios;
 private final EstablecimientoRepository establecimientos;
 private final OrganizacionBeneficiariaRepository organizaciones;
 private final ProductoRepository productos;
 private final LoteExcedenteRepository lotes;
 private final PasswordEncoder encoder;
 @Value("${foodloop.demo.enabled:false}") private boolean habilitado;
 @Value("${foodloop.demo.password:}") private String password;
 private Usuario user(String nombre,String rol){
     Usuario u=usuarios.findByUsername(nombre);
     if(u!=null)return u;
     u=new Usuario();u.setUsername(nombre);u.setPassword(encoder.encode(password));u.setNombreCompleto(nombre+" (demostración)");u.setRol(rol);u.setVerificado(true);return usuarios.save(u);
 }
 @Override
    public void run(String... args) {
     if(!habilitado)return;
     if(password==null||password.length()<8)throw new IllegalStateException("La contraseña demo debe tener 8 caracteres o más");
     Usuario seller=user("comercio_demo","VENDEDOR");user("comprador_demo","COMPRADOR");Usuario orgUser=user("ong_demo","ORGANIZACION");user("admin_demo","ADMIN");
     OrganizacionBeneficiaria o=organizaciones.findByUsuarioId(orgUser.getId()).orElseGet(OrganizacionBeneficiaria::new);
     o.setUsuarioId(orgUser.getId());o.setRazonSocial("Comedor Esperanza (demo)");o.setDistrito("Lima");o.setDireccion("Dirección de prueba, Lima");o.setCapacidadRaciones(100);o.setRefrigeracion(true);o.setTransportePropio(true);o.setVerificada(true);organizaciones.save(o);
     Establecimiento e=establecimientos.findAll().stream().filter(c->seller.getId().equals(c.getUsuarioResponsableId())).findFirst().orElse(null);
     if(e==null){e=new Establecimiento();e.setUsuarioResponsableId(seller.getId());e.setNombreComercial("Panadería Origen (demo)");e.setDireccion("Dirección ilustrativa, Miraflores");e.setDistrito("Miraflores");e.setHorarioAtencion("09:00 a 18:00");e.setHorarioRecojo("15:00 a 18:00");e.setVende(true);e.setDona(true);e=establecimientos.save(e);}
     if(lotes.findByPropietarioIdOrderByIdDesc(seller.getId()).isEmpty()){
         crear(seller,e,"Pack de panes del día","Panadería","VENTA",14,6.0,13.0,"Ambiente protegido","Gluten");
         crear(seller,e,"Frutas de temporada","Frutas y verduras","DONACION",20,0d,0d,"Ambiente fresco","Ninguno declarado");
         crear(seller,e,"Almuerzos excedentes","Comida preparada","DONACION",10,0d,0d,"Refrigerado","Consultar ingredientes");
     }
 }
 private void crear(Usuario u,Establecimiento e,String name,String cat,String modo,int qty,double price,double original,String storage,String allergens){
     Producto p=new Producto();p.setNombre(name);p.setCategoria(cat);p.setPrecioDescuento(price);p.setPrecioOriginal(original);p.setStock(qty);p.setEstablecimiento(e);p.setAlergenos(allergens);p.setDescripcion("Alimento de demostración. Confirmar inocuidad antes de uso real.");p.setPesoPorUnidadKg(.25);productos.save(p);
     LoteExcedente l=new LoteExcedente();l.setProducto(p);l.setEstablecimiento(e);l.setPropietarioId(u.getId());l.setModalidad(modo);
     l.setStockInicial(qty);l.setStockDisponible(qty);l.setStockReservado(0);l.setPrecioOriginal(original);l.setPrecioLiquidacion(price);
     l.setPesoPorUnidadKg(.25);l.setFechaPreparacion(LocalDateTime.now(java.time.ZoneId.of("America/Lima")).minusMinutes(30));l.setLimiteReserva(LocalDateTime.now(java.time.ZoneId.of("America/Lima")).plusHours(3));
     l.setLimiteRecojo(LocalDateTime.now(java.time.ZoneId.of("America/Lima")).plusHours(5));l.setFechaVencimiento(LocalDateTime.now(java.time.ZoneId.of("America/Lima")).plusHours(8));l.setConservacion(storage);lotes.save(l);
 }

    // Metodos escritos de forma explicita, sin Lombok.
    public DatosDemo(UsuarioRepository usuarios, EstablecimientoRepository establecimientos, OrganizacionBeneficiariaRepository organizaciones, ProductoRepository productos, LoteExcedenteRepository lotes, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.establecimientos = establecimientos;
        this.organizaciones = organizaciones;
        this.productos = productos;
        this.lotes = lotes;
        this.encoder = encoder;
    }

}
