package br.com.alura.leilao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import br.com.alura.leilao.exception.UsuarioDeuCincoLancesException;

public class Leilao implements Serializable {

    private final String descricao;
    private final List<Lance> lances;

    private double maiorLance = 0;
    private double menorLance = 0;

    public Leilao(String descricao) {
        this.descricao = descricao;
        this.lances = new ArrayList<> ();
    }

    public void propoe(Lance lance) {
        if (lanceNaoValido ( lance )) return;

        lances.add ( lance );

        Collections.sort ( lances );

        defineMaiorEMenorLance ( lance );
        calculaMaiorLance ( lance );
        calculaMenorLance ( lance );
    }

    private void defineMaiorEMenorLance(Lance lance) {
        if (lances.size () == 1) {
            maiorLance = lance.getValor ();
            menorLance = lance.getValor ();
        }
    }

    private boolean lanceNaoValido(Lance lance) {
        if (lanceForMenorQueOUltimoLance ( lance, maiorLance ))
            throw new LanceMenorQueUltimoLanceException ( "Lance foi menor que maior lance" );

        if (!lances.isEmpty ()) {
            final Usuario usuarioNovo = lance.getUsuario ();
            if (usuarioForOMesmoDoUltimoLance ( usuarioNovo ))
                throw new LanceSeguidoDoMesmoUsuarioException ( "Mesmo usuário do ultimo lance" );
            if (usuarioDeuCincoLances ( usuarioNovo ))
                throw new UsuarioDeuCincoLancesException ( "Usuario deu 5 lances" );
        }
        return false;
    }

    private boolean usuarioDeuCincoLances(Usuario usuarioNovo) {
        int lancesDoUsuario = 0;
        for (Lance l : lances) {
            Usuario usuarioExistente = l.getUsuario ();
            if (usuarioExistente.equals ( usuarioNovo )) {
                lancesDoUsuario++;
            }
        }

        if (lancesDoUsuario >= 5) {
            return true;
        }
        return false;
    }

    private boolean usuarioForOMesmoDoUltimoLance(Usuario usuarioNovo) {
        Usuario usuarioUltimoLance = this.lances.get ( 0 ).getUsuario ();

        if (usuarioNovo.equals ( usuarioUltimoLance )) {
            return true;
        }
        return false;
    }

    private boolean lanceForMenorQueOUltimoLance(Lance lance, double maiorLance) {
        return lance.getValor () < maiorLance;
    }

    private void calculaMenorLance(Lance lance) {
        if (lance.getValor () < menorLance) {
            this.menorLance = lance.getValor ();
        }
    }

    private void calculaMaiorLance(Lance lance) {
        if (lance.getValor () > maiorLance) {
            this.maiorLance = lance.getValor ();
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public double getMaiorLance() {
        return this.maiorLance;
    }

    public double getMenorLance() {
        return this.menorLance;
    }

    public List<Lance> tresMaioresLances() {
        return this.lances.subList ( 0, lances.size () > 3 ? 3 : lances.size () );
    }

    public int quantidadeLances() {
        return lances.size ();
    }
}