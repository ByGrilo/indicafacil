# Mapa de Wireframes - IndicaFacil

## Objetivo

Este documento relaciona as telas do wireframe com a interface grafica e as classes principais do projeto.

## Telas e correspondencia no codigo

### 1. Entre

- Painel Swing: `src/indicafacil/ui/LoginPanel.java`
- Janela principal: `src/indicafacil/ui/IndicaFacilFrame.java`
- Regra de autenticacao: `src/indicafacil/auth/service/AutenticacaoService.java`

### 2. Cadastre-se

- Painel Swing: `src/indicafacil/ui/CadastroPanel.java`
- Dados suportados: nome, e-mail, telefone, CPF e senha
- Persistencia: `src/indicafacil/auth/persistence/UsuarioRepository.java`

### 3. Esqueceu sua senha

- Dialogo Swing: `showForgotPasswordDialog()`
- Regras: `solicitarRecuperacaoSenha()` e `redefinirSenhaPorEmail()`
- Observacao: o envio de e-mail esta simulado por dialogo

### 4. Home

- Painel Swing: `src/indicafacil/ui/HomePanel.java`
- Dados exibidos:
  - mais buscados
  - favoritos
  - atalho para busca, detalhe, avaliacao e ranking
- Regra principal: `src/indicafacil/service/IndicaFacilService.java`

### 5. Lista e busca de profissionais

- Painel Swing: `src/indicafacil/ui/SearchPanel.java`
- Busca por texto: `buscarPorTexto()`
- Filtro por categoria: aplicado no proprio painel de busca

### 6. Detalhe do profissional

- Dialogo Swing: `showWorkerDetailDialog()`
- Dados exibidos:
  - nome
  - categoria
  - empresa
  - descricao
  - media
  - avaliacoes
  - status de favorito

### 7. Atividade

- Painel Swing: `src/indicafacil/ui/ActivityPanel.java`
- Dados carregados por: `listarAtividadeDoUsuario()`
- Repositorio: `src/indicafacil/persistence/AvaliacaoRepository.java`

### 8. Perfil

- Painel Swing: `src/indicafacil/ui/ProfilePanel.java`
- Dados exibidos:
  - nome
  - e-mail
  - telefone
  - tipo de conta
  - perfil profissional

### 9. Configuracoes

- Painel Swing: `src/indicafacil/ui/SettingsPanel.java`
- Subtelas:
  - sua conta
  - privacidade
  - aparencia
  - sobre
  - sair da conta

## Classes novas ligadas aos wireframes

- `src/indicafacil/ui/IndicaFacilFrame.java`
- `src/indicafacil/ui/LoginPanel.java`
- `src/indicafacil/ui/CadastroPanel.java`
- `src/indicafacil/ui/MainMenuPanel.java`
- `src/indicafacil/ui/HomePanel.java`
- `src/indicafacil/ui/SearchPanel.java`
- `src/indicafacil/ui/ActivityPanel.java`
- `src/indicafacil/ui/ProfilePanel.java`
- `src/indicafacil/ui/SettingsPanel.java`
- `src/indicafacil/ui/ProfessionalCardPanel.java`
- `src/indicafacil/ui/SwingDialogs.java`
- `src/indicafacil/ui/UITheme.java`
- `src/indicafacil/ui/RoundedPanel.java`
- `src/indicafacil/ui/RoundedButton.java`
- `src/indicafacil/auth/util/EmailUtils.java`
- `src/indicafacil/auth/util/TelefoneUtils.java`
- `src/indicafacil/model/AtividadeUsuario.java`
- `src/indicafacil/persistence/FavoritoRepository.java`

## Observacao final

O projeto continua em Java, mas o fluxo principal agora e grafico em Swing, aproveitando a logica existente e aproximando a aplicacao das telas desenhadas no wireframe.
