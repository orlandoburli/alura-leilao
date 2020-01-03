package br.com.alura.leilao.model;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import br.com.alura.leilao.exception.UsuarioDeuCincoLancesException;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LeilaoTest {

    public static final double DELTA = 0.0001;
    private final Usuario alex = new Usuario ( "Alex" );
    private final Usuario fran = new Usuario ( "Fran" );

    private final Leilao console = new Leilao ( "Console" );
    private final Leilao carro = new Leilao ( "Carro" );
    private final Leilao computador = new Leilao ( "Computador" );

    @Rule
    public ExpectedException exception = ExpectedException.none ();

    @Test
    public void deveGuardarDescricao() {
        assertEquals ( "Console", console.getDescricao () );
        assertThat ( console.getDescricao (), equalTo ( "Console" ) );
        assertThat ( console.getDescricao (), is ( "Console" ) );

        assertThat ( 4.1 + 5.3, closeTo ( 4.4 + 5.0, DELTA ) );
    }

    @Test
    public void deveGuardarMaiorLanceQuandoRecebeApenasUmLance() {
        console.propoe ( new Lance ( alex, 199.32 ) );

        assertEquals ( 199.32, console.getMaiorLance (), DELTA );
    }

    @Test
    public void deveGuardarMenorLanceQuandoRecebeApenasUmLance() {
        console.propoe ( new Lance ( alex, 199.32 ) );

        assertEquals ( 199.32, console.getMenorLance (), DELTA );
    }

    @Test
    public void deveGuardarMaiorLanceRecebendo2LancesCrescentes() {
        computador.propoe ( new Lance ( alex, 100 ) );
        computador.propoe ( new Lance ( fran, 200 ) );

        assertEquals ( 200, computador.getMaiorLance (), DELTA );
    }

    @Test
    public void deveGuardarMenorLanceRecebendo2LancesCrescentes() {
        computador.propoe ( new Lance ( alex, 100 ) );
        computador.propoe ( new Lance ( fran, 200 ) );

        assertEquals ( 100, computador.getMenorLance (), DELTA );
    }

    @Test
    public void deveGuardarMaiorLanceRecebendo2LancesDecrescentes() {
        carro.propoe ( new Lance ( alex, 1500 ) );
        carro.propoe ( new Lance ( fran, 2500 ) );

        assertEquals ( 2500, carro.getMaiorLance (), DELTA );
    }

    @Test
    public void deveGuardarMenorLanceRecebendo2LancesDecrescentes() {
        carro.propoe ( new Lance ( alex, 1500 ) );
        carro.propoe ( new Lance ( fran, 2500 ) );

        assertEquals ( 1500, carro.getMenorLance (), DELTA );
    }

    @Test
    public void deveDevolverTresMaioresLancesQuandoRecebeExatosTresLances() {
        console.propoe ( new Lance ( alex, 200 ) );
        console.propoe ( new Lance ( fran, 300 ) );
        console.propoe ( new Lance ( alex, 400 ) );

        List<Lance> tresMaioresLances = console.tresMaioresLances ();

        assertNotNull ( tresMaioresLances );

        assertThat ( tresMaioresLances, hasSize ( equalTo ( 3 ) ) );

        assertThat ( tresMaioresLances, hasItem ( new Lance ( alex, 400 ) ) );

        assertThat ( tresMaioresLances, both (
                Matchers.<Lance>hasSize ( 3 ) )
                .and ( contains (
                        new Lance ( alex, 400 ),
                        new Lance ( fran, 300 ),
                        new Lance ( alex, 200 )
                        )
                )
        );

        assertEquals ( 3, tresMaioresLances.size () );
        assertEquals ( 400, tresMaioresLances.get ( 0 ).getValor (), DELTA );
        assertEquals ( 300, tresMaioresLances.get ( 1 ).getValor (), DELTA );
        assertEquals ( 200, tresMaioresLances.get ( 2 ).getValor (), DELTA );
    }

    @Test
    public void deveDevolverTresMaioresLancesQuandoNaoRecebeLances() {
        List<Lance> tresMaioresLances = console.tresMaioresLances ();

        assertNotNull ( tresMaioresLances );
        assertEquals ( 0, tresMaioresLances.size () );
    }

    @Test
    public void deveDevolverTresMaioresLancesQuandoRecebeApenasUmLance() {
        console.propoe ( new Lance ( alex, 200 ) );

        List<Lance> tresMaioresLances = console.tresMaioresLances ();

        assertNotNull ( tresMaioresLances );
        assertEquals ( 1, tresMaioresLances.size () );
        assertEquals ( 200, tresMaioresLances.get ( 0 ).getValor (), DELTA );
    }

    @Test
    public void deveDevolverTresMaioresLancesQuandoRecebeApenasDoisLances() {
        console.propoe ( new Lance ( alex, 200 ) );
        console.propoe ( new Lance ( fran, 300 ) );

        List<Lance> tresMaioresLances = console.tresMaioresLances ();

        assertNotNull ( tresMaioresLances );
        assertEquals ( 2, tresMaioresLances.size () );
        assertEquals ( 300, tresMaioresLances.get ( 0 ).getValor (), DELTA );
    }

    @Test
    public void deveDevolverTresMaioresLancesQuandoRecebeQuatroLances() {
        console.propoe ( new Lance ( alex, 200 ) );
        console.propoe ( new Lance ( fran, 300 ) );
        console.propoe ( new Lance ( alex, 400 ) );
        console.propoe ( new Lance ( fran, 500 ) );

        List<Lance> tresMaioresLances = console.tresMaioresLances ();

        assertNotNull ( tresMaioresLances );
        assertEquals ( 3, tresMaioresLances.size () );
        assertEquals ( 500, tresMaioresLances.get ( 0 ).getValor (), DELTA );
    }

    @Test
    public void deveDevolverValorZeroParaMaiorLanceQuandoNaoTiverLance() {
        assertEquals ( 0.0, console.getMaiorLance (), DELTA );
    }

    @Test
    public void deveDevolverValorZeroParaMenorLanceQuandoNaoTiverLance() {
        assertEquals ( 0.0, console.getMaiorLance (), DELTA );
    }

    @Test
    public void naoDeveAdicionarLanceQuandoForMenorQueOMaiorLance() {
        exception.expect ( LanceMenorQueUltimoLanceException.class );
        exception.expectMessage ( "Lance foi menor que maior lance" );

        console.propoe ( new Lance ( alex, 500 ) );
        console.propoe ( new Lance ( fran, 400 ) );

        assertEquals ( 1, console.quantidadeLances () );
    }

    @Test
    public void naoDeveAdicionarLanceQuandoForOMesmoUsuarioDoUltimoLance() {
        exception.expect ( LanceSeguidoDoMesmoUsuarioException.class );
        exception.expectMessage ( "Mesmo usuário do ultimo lance" );

        console.propoe ( new Lance ( new Usuario ( "Alex" ), 500 ) );
        console.propoe ( new Lance ( new Usuario ( "Alex" ), 600 ) );

        fail ( "Não deveria ter adicionado o lance pelo mesmo usuario" );
    }

    @Test
    public void naoDeveAdicionarLanceQuandoUsuarioDerCincoLances() {
        exception.expect ( UsuarioDeuCincoLancesException.class );
        exception.expectMessage ( "Usuario deu 5 lances" );

        console.propoe ( new Lance ( alex, 100 ) );
        console.propoe ( new Lance ( fran, 200 ) );
        console.propoe ( new Lance ( alex, 300 ) );
        console.propoe ( new Lance ( fran, 400 ) );
        console.propoe ( new Lance ( alex, 500 ) );
        console.propoe ( new Lance ( fran, 600 ) );
        console.propoe ( new Lance ( alex, 700 ) );
        console.propoe ( new Lance ( fran, 800 ) );
        console.propoe ( new Lance ( alex, 900 ) );
        console.propoe ( new Lance ( fran, 1000 ) );

        console.propoe ( new Lance ( alex, 1100 ) );
        fail ( "Usuario nao deveria dar o 6 lance" );
    }
}