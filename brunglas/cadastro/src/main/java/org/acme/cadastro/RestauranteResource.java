package org.acme.cadastro;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @GET
    public List<Restaurante> buscarTodos() {
        return Restaurante.listAll();
    }

    @GET
    @Path("/{id}")
    public Restaurante buscaRestaurante(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurante restaurante = restauranteOp.get();
        return restaurante;
    }

    @POST
    @Transactional
    public void adicionar(Restaurante dto) {
        dto.persist();
        Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void adicionar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurante restaurante = restauranteOp.get();
        restaurante.nome = dto.nome;
        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteRestaurante(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {throw new NotFoundException();});
    }

    @GET
    @Path("{idRestaurante}/pratos")
    public List<Restaurante> buscaTodosPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }
        return Prato.list("restaurante", restauranteOp.get());
    }

    @GET
    @Path("{idRestaurante}/pratos/{id}")
    public Prato buscaPrato(@PathParam("id") Long idRestaurante, Long id) {
        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        pratoOp.ifPresentOrElse(Prato::findById, () -> {throw new NotFoundException();});
        Prato prato = pratoOp.get();
        return prato;
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    public Response adicionaPrato(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
        Optional<Restaurante> restauranteop = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteop.isEmpty()) {
            throw new NotFoundException();
        }
/**
*       Criando um novo prato, que é a entidade diretamente a ser persistida. passando o dto, atribuindo os valores do
*       dto para a entidade que possui o método de persistir no banco.
 */

        Prato prato = new Prato();
        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.preco = dto.preco;

/**
 *      Associando o prato criado acima, com os atributos que virão do endpoint ao restaurante passado também
 *      no @Path do endpoint
 *      E, por fim, persistindo a entidade prato, que já contem o restaurante.
 */
        prato.restaurante = restauranteop.get();
        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }
}
