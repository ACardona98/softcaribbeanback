package pruebas.back;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pruebas.back.entidades.Respuesta;
import pruebas.back.entidades.Usuario;
import pruebas.back.util.CodigosRespuestas;
import pruebas.back.util.JPAUtils;
import pruebas.back.util.Ordering;
import pruebas.back.util.Validaciones;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE})
public class UsuarioController {
	
	private UsuarioDAO dao = new UsuarioDAOImpl();
	
	@PostMapping("/guardarUsuario")
	public Respuesta guardarUsuario(@RequestBody Usuario usuario) {
		String mensajeError = "";
		if(!Validaciones.isValidEmailAddress(usuario.getCorreo())) {
			mensajeError = "Email no válido";
		}
		if(Validaciones.contieneLetras(usuario.getCedula())) {
			mensajeError = "La Cédula debe ser un número";
		}
		if(Validaciones.contieneLetras(usuario.getTelefono())) {
			mensajeError = "El teléfono debe ser un número";
		}
		
		if(mensajeError.length() > 0) {
			return new Respuesta(CodigosRespuestas.ERR, mensajeError);
		}
		
		return dao.save(usuario);
	}
	
	@GetMapping("/getUsuarios")
	public List<Usuario> getUsuarios(@RequestParam("orden") int orden) {
		return dao.findAll(orden == 1 ? Ordering.DESC : Ordering.ASC);
	}	
	
	@GetMapping("/getUsuario/{id}")
	public Usuario getUsuario(@PathVariable("id") int idUsuario) {
		return dao.findOne(idUsuario);
	}
	
	@DeleteMapping("/eliminarUsuario/{id}")
	public Respuesta eliminarUsuario(@PathVariable("id") int idUsuario) {
		return dao.delete(idUsuario);
	}
}
