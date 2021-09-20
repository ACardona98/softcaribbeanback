package pruebas.back;

import java.util.List;

import pruebas.back.entidades.Respuesta;
import pruebas.back.entidades.Usuario;
import pruebas.back.util.Ordering;

public interface UsuarioDAO {
	List<Usuario> findAll(Ordering orden);
	Usuario findOne(int idUsuario);
	Respuesta save(Usuario usuario);
	Respuesta delete(int idUsuario);
}
