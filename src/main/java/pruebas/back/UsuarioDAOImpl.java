package pruebas.back;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import pruebas.back.entidades.Respuesta;
import pruebas.back.entidades.Usuario;
import pruebas.back.util.BinaryTree;
import pruebas.back.util.CodigosRespuestas;
import pruebas.back.util.JPAUtils;
import pruebas.back.util.OrderMethod;
import pruebas.back.util.Ordering;

public class UsuarioDAOImpl implements UsuarioDAO{
	
	public UsuarioDAOImpl() {
		if(BinaryTree.getUserTree().isEmpty()) {
			EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
			List<Usuario> usuarios = em.createNativeQuery("SELECT * FROM USUARIO", Usuario.class).getResultList();
			BinaryTree.getUserTree().insert(usuarios);
		}
	}
	
	@Override
	public List<Usuario> findAll(Ordering orden) {
		
		return BinaryTree.getUserTree().getData(new OrderMethod<Usuario>() {
			@Override
			public void order(BinaryTree<Usuario> bt, ArrayList<Usuario> data) {
				switch(orden) {
					case ASC:
						BinaryTree.preorden(bt, data);
						break;
					case DESC:
						BinaryTree.postorden(bt, data);
						break;
				}
				
			}
		});
	}

	@Override
	public Usuario findOne(int idUsuario) {
		Usuario usuario = BinaryTree.getUserTree().find(idUsuario);
		return usuario;
	}

	@Override
	public Respuesta save(Usuario usuario) {
		Respuesta r;
		
		//crear en la base de datos
		
		EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
		
		StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("SP_GUARDAR_USUARIO");
	    // set parameters
	    storedProcedure.registerStoredProcedureParameter("pIdUsuario", Integer.class, ParameterMode.INOUT);
	    storedProcedure.registerStoredProcedureParameter("pNombres", String.class, ParameterMode.IN);
	    storedProcedure.registerStoredProcedureParameter("pApellidos", String.class, ParameterMode.IN);
	    storedProcedure.registerStoredProcedureParameter("pCedula", String.class, ParameterMode.IN);
	    storedProcedure.registerStoredProcedureParameter("pCorreo", String.class, ParameterMode.IN);
	    storedProcedure.registerStoredProcedureParameter("pTelefono", String.class, ParameterMode.IN);
	    storedProcedure.registerStoredProcedureParameter("respuesta", Integer.class, ParameterMode.OUT);
	    storedProcedure.setParameter("pIdUsuario", usuario.getIdUsuario());
	    storedProcedure.setParameter("pNombres", usuario.getNombres());
	    storedProcedure.setParameter("pApellidos", usuario.getApellidos());
	    storedProcedure.setParameter("pCedula", usuario.getCedula());
	    storedProcedure.setParameter("pCorreo", usuario.getCorreo());
	    storedProcedure.setParameter("pTelefono", usuario.getTelefono());
	    
	    Integer resp;
	    try{
	    	// execute stored procedure        
		    storedProcedure.execute();
	    	resp = (Integer)storedProcedure.getOutputParameterValue("respuesta");
	    	
	    	//si el resultado es OK
	    	if (resp == CodigosRespuestas.OK) {
	    		if(usuario.getIdUsuario() == null) {
	    			//si es de creación obtener id e insertar usuario en el árbol, no es posible insertarlo antes ya que se debe obtener el Id
	    			usuario.setIdUsuario((Integer)storedProcedure.getOutputParameterValue("pIdUsuario"));
			    	BinaryTree.getUserTree().insert(usuario);
	    		}else {
	    			//si es modificación modificar los datos del elemento del arbol por el actualizado
		    		Usuario u = BinaryTree.getUserTree().find(usuario.getIdUsuario());
		    		u.setCedula(usuario.getCedula());
		    		u.setNombres(usuario.getNombres());
		    		u.setApellidos(usuario.getApellidos());
		    		u.setCorreo(usuario.getCorreo());
		    		u.setTelefono(usuario.getTelefono());
	    		}	    		
	    	}
	    }catch(Exception e) {
	    	resp = CodigosRespuestas.ERR;
	    }
		em.close();
		
		switch (resp) {
		case CodigosRespuestas.OK:
			r = new Respuesta(CodigosRespuestas.OK, "Se ha guardado satisfactoriamente");
			break;
		default:
			r = new Respuesta(CodigosRespuestas.ERR, "El Correo o Cédula ya se encuentran registradas");
			break;
		}
		return r;
	}

	@Override
	public Respuesta delete(int idUsuario) {
		EntityManager em = JPAUtils.getEntityManagerFactory().createEntityManager();
		Respuesta r;
		try {
			//eliminar del árbol
			BinaryTree.getUserTree().delete(idUsuario);
			
			//eliminar de la base de datos
			em.getTransaction().begin();
			em.remove(em.merge(findOne(idUsuario)));
			em.getTransaction().commit();
			r = new Respuesta(CodigosRespuestas.OK, "Se ha eliminado el usuario cod: " + idUsuario);
		}catch(Exception e) {
			e.printStackTrace();
			r = new Respuesta(CodigosRespuestas.ERR, "Ha ocurrido un error"); 
		}
		return r;
	}
	
}
