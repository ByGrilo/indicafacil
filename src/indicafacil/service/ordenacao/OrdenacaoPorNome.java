package indicafacil.service.ordenacao;

import indicafacil.model.PerfilTrabalhador;
import java.util.List;

public class OrdenacaoPorNome extends AbstractOrdenacaoProfissional {
    @Override
    public String getRotulo() {
        return "Nome";
    }

    @Override
    public List<PerfilTrabalhador> ordenar(List<PerfilTrabalhador> profissionais) {
        List<PerfilTrabalhador> copia = copiarLista(profissionais);
        copia.sort((primeiro, segundo) -> primeiro.getNome().compareToIgnoreCase(segundo.getNome()));
        return copia;
    }
}
