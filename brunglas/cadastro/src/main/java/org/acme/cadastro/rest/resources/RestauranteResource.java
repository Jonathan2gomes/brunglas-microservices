package org.acme.cadastro.rest.resources;

import org.acme.cadastro.domain.Prato;
import org.acme.cadastro.domain.Restaurante;
import org.acme.cadastro.rest.constraints.ConstraintViolationResponse;
import org.acme.cadastro.rest.dto.RestauranteDTO;
import org.acme.cadastro.rest.mapper.RestauranteMapper;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurante")
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "brunglas-oauth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/brunglas/protocol/openid-connect/token")))
@SecurityRequirement(name = "brunglas-oauth", scopes = {})
public class RestauranteResource {

    @Inject
    RestauranteMapper restauranteMapper;

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
    @APIResponse(responseCode = "201", description = "Caso restaurante seja encontrado com sucesso")
    @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    public void adicionar(@Valid RestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toRestaurante(dto);
        restaurante.persist();
        Response.status(Response.Status.CREATED).build();
    }

    /**
     * para o put funcionar, primeiramente temos que passar o id e fazer um find, pra
     * descobrir qual é o produto ou prato que desejamos alterar. Utilizamos o findByIdOptional, pois
     * já previnimos caso não exista aquele id, acredito que isso também evite o famoso nullPointerException.
     *
     * Depois de buscar o id do objeto que desejamos alterar, instanciamos um bovo objeto, fazendo com que esse objeto
     * receba o objeto busacdo pelo id, que no caso será restauranteOP.get();
     *
     * Em seguida, atribuimos os atributos permitidos pelo endpoint, por exemplo, o nome:
     * restaurante.nome = objeto.atributo do parâmetro (restaurante.nome = dto.nome)
     *
     * Em seguida, basta persistirmos o objeto (restaurante.persist).
     *
     * Vale lembrar que sempre que quisermos fazer algo que não seja buscar dados no banco, devemos utilizar
     * a anotação @Transactional para dizer ao hibernate que ele tem permissão para fazer alterações no banco.
     */

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
    @Tag(name = "Prato")
    public List<Restaurante> buscaTodosPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }
        return Prato.list("restaurante", restauranteOp.get());
    }

    @GET
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "Prato")
    public Prato buscaPrato(@PathParam("id") Long idRestaurante, Long id) {
        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        pratoOp.ifPresentOrElse(Prato::findById, () -> {throw new NotFoundException();});
        Prato prato = pratoOp.get();
        return prato;
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "Prato")
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

    @PUT
    @Path("{idRestaurante}/prato/{id}")
    @Transactional
    @Tag(name = "Prato")
    public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }
        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        if(pratoOp.isEmpty()) {
            throw new NotFoundException("Prato não existe");
        }
        Prato prato = new Prato();
        prato.preco = dto.preco;
        prato.persist();
    }

    @DELETE
    @Path("{idRestaurante}/prato/{id}")
    @Transactional
    @Tag(name = "Prato")
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if(restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        Optional<Prato> pratoOp = Prato.findByIdOptional(id);

        pratoOp.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException("Prato não encontrado");
        });
    }

}
