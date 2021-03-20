package org.acme.cadastro.rest.constraints;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class ConstraintViolationImplementation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Schema(description = "Path do atributo, ex: dataInicio, pessoa.endereco.numero", required = false)
    private final String atributo;

    @Schema(description = "Mensagem descritiva do erro possivelmente associado ao path", required = true)
    private final String mensagem;

    private ConstraintViolationImplementation(ConstraintViolation<?> violation) {
        this.mensagem = violation.getMessage();
        this.atributo = Stream.of(violation.getPropertyPath().toString().split("\\.")).skip(2).collect(Collectors.joining("."));
    }

    public ConstraintViolationImplementation(String atributo, String mensagem) {
        this.mensagem = mensagem;
        this.atributo = atributo;
    }

    public static ConstraintViolationImplementation of(ConstraintViolation<?> violation) {
        return new ConstraintViolationImplementation(violation);
    }

    public static ConstraintViolationImplementation of(String violation) {
        return new ConstraintViolationImplementation(null, violation);
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getAtributo() {
        return atributo;
    }

}
