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

import org.biblioteca.abm.session.AutorSession;
import org.biblioteca.entidad.Autor;

@Path("autor")
public class AutorNormalizadoRestService {
@EJB AutorSession as;
	

//GET http://localhost:8080/PROYECTO-REST/rest/autor/list
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Map<String, Object> listar() throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("success", true);
		retorno.put("result", as.buscarTodos());
		return retorno;
	}

//GET http://localhost:8080/PROYECTO-REST/rest/autor/find/n
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find/{codigo}")
	public Map<String, Object> buscar(@PathParam("codigo") Integer codigo) throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("success", true);
		retorno.put("result", as.buscarPorCodigo(codigo));
		return retorno;
	}

//PUT http://localhost:8080/PROYECTO-REST/rest/autor/update
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Map<String, Object> actualizar(Autor autor) throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("success", true);
		retorno.put("result", as.actualizar(autor));
		return retorno;
	}

//DELETE http://localhost:8080/PROYECTO-REST/rest/autor/delete/n
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete/{codigo}")
	public Map<String, Object> borrar(@PathParam("codigo") Integer codigo) throws Exception {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("success", true);
		as.eliminar(codigo);
		return retorno;
	}
}