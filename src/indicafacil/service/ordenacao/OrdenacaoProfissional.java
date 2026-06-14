package indicafacil.service.ordenacao;

import indicafacil.model.PerfilTrabalhador;
import java.util.List;

/*
 * Essa interface representa um jeito de ordenar a lista de profissionais.
 * A tela escolhe uma estrategia e o service aplica sem precisar saber detalhes.
 */
public interface OrdenacaoProfissional {
    String getRotulo();

    List<PerfilTrabalhador> ordenar(List<PerfilTrabalhador> profissionais);
}
