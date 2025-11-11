package com.socialseed.authservice.platform.error;

/**
 * Excepción de negocio genérica.
 * Siempre asociada a un ErrorCode y opcionalmente a parámetros dinámicos para mensajes.
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] params;

    public BusinessException(ErrorCode errorCode, Object... params) {
        super(errorCode.getCode()); // solo por compatibilidad interna
        this.errorCode = errorCode;
        this.params = params;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getParams() {
        return params;
    }
}
