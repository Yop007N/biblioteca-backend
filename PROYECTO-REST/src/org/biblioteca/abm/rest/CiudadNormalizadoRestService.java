package org.biblioteca.abm.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.biblioteca.abm.session.CiudadSession;
import org.biblioteca.entidad.Ciudad;

//http://localhost:8080/PROYECTO-REST/rest/ciudad/list
@Path("ciudad")
public class CiudadNormalizadoRestService {
	@EJB
	CiudadSession cs;//Inyeccion de dependencias

//GET http://localhost:8080/PROYECTO-REST/rest/ciudad/list
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Map<String, Object> listar() throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>(); //{success: true, result: arraylist}
		
		retorno.put("success", true);
		retorno.put("result", cs.buscarTodos());
		return retorno;
	}

//GET http://localhost:8080/PROYECTO-REST/rest/ciudad/find/variable
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find/{codigo}")
	public Map<String, Object> buscar(@PathParam("codigo") Integer codigo) throws Exception {
		
		
		Map<String, Object> retorno = new HashMap<String, Object>(); 
		retorno.put("success", true);
		retorno.put("result", cs.buscarPorCodigo(codigo));
		return retorno;
	}

//PUT http://localhost:8080/PROYECTO-REST/rest/ciudad/update
	@PUT	//Verbo //Method
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Map<String, Object> actualizar(Ciudad ciudad) throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>(); 
		retorno.put("success", true);
		retorno.put("result", cs.actualizar(ciudad));
		
		return retorno;
	}

//DELETE http://localhost:8080/PROYECTO-REST/rest/ciudad/delete/n
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete/{codigo}")
	public Map<String, Object> borrar(@PathParam("codigo") Integer codigo) throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>(); 
		try {
			cs.eliminar(codigo);
			retorno.put("success", true);
			
		} catch (Exception e) {
			retorno.put("success", false);
			retorno.put("result", "El registro est� siendo utilizado " + e.getCause().getCause().getCause().getCause().getMessage());
		}
		
		return retorno;
	}
}
//Tipo de objeto que se retorna hacia afuera
/*{
	success : true | false,
	result : (objeto de cualquier tipo) o el mensaje si success = false
}*/
