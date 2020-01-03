package br.com.alura.leilao.exception;

public class LanceSeguidoDoMesmoUsuarioException extends RuntimeException {

    public LanceSeguidoDoMesmoUsuarioException(String message) {
        super ( message );
    }
}
