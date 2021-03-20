package org.acme.cadastro.rest.constraints;



import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintViolationResponse {

    private final List<ConstraintViolationImplementation> violacoes = new ArrayList<>();

    private ConstraintViolationResponse(ConstraintViolationException exception) {
        exception.getConstraintViolations().forEach(violation -> violacoes.add(ConstraintViolationImplementation.of(violation)));
    }

    public static Object of(ConstraintViolationException exception) {
        return new ConstraintViolationResponse(exception);
    }

    public List<ConstraintViolationImplementation> getViolacoes() {
        return violacoes;
    }
}
