package indicafacil.service.ordenacao;

import indicafacil.model.PerfilTrabalhador;
import java.util.ArrayList;
import java.util.List;

/*
 * Essa base ajuda as estrategias de ordenacao a reaproveitar umas partes comuns.
 * Cada subclasse muda so o criterio da ordenacao mesmo.
 */
public abstract class AbstractOrdenacaoProfissional implements OrdenacaoProfissional {
    protected List<PerfilTrabalhador> copiarLista(List<PerfilTrabalhador> profissionais) {
        return new ArrayList<>(profissionais);
    }

    @Override
    public String toString() {
        return getRotulo();
    }
}
