# IndicaFacil

Aplicativo desktop em Java para conectar clientes e prestadores de servico por meio de busca, favoritos e avaliacoes.

O projeto foi desenvolvido com `Java + Swing + SQLite`, mantendo a interface grafica separada da regra de negocio.

## Visao geral

O `IndicaFacil` nasceu com a ideia de facilitar a procura por profissionais autonomos e prestadores de servico, usando feedback de outros usuarios como apoio na escolha. O sistema permite criar conta, fazer login, cadastrar perfil profissional, buscar trabalhadores e registrar avaliacoes.

## Tecnologias usadas

- `Java 17`
- `Swing`
- `SQLite`
- `Maven`

## Funcionalidades principais

- Cadastro de conta com `nome`, `e-mail`, `telefone`, `cpf` e `senha`
- Login com `e-mail ou CPF`
- Recuperacao de senha em fluxo interno
- Validacao de CPF, e-mail e telefone
- Senha protegida com hash `SHA-256`
- Persistencia em banco `SQLite`
- Cadastro de perfil de prestador de servico
- Busca de profissionais por texto e categoria
- Favoritos
- Avaliacoes com opcao de anonimato
- Tela de atividade com historico das avaliacoes feitas

## Estrutura do projeto

- `src/indicafacil/app`: inicializacao da aplicacao
- `src/indicafacil/ui`: telas Swing, frame principal e componentes visuais
- `src/indicafacil/auth/model`: modelo da conta do usuario
- `src/indicafacil/auth/service`: regras de autenticacao
- `src/indicafacil/auth/persistence`: conexao com SQLite e repositorio de usuarios
- `src/indicafacil/auth/util`: validacoes e utilitarios
- `src/indicafacil/persistence`: repositorios de trabalhador, favorito e avaliacao
- `src/indicafacil/service`: regra de negocio principal do sistema
- `src/indicafacil/model`: modelos usados pelas telas e servicos

## Interface

As telas visuais foram organizadas com base nos wireframes do projeto e incluem:

- `Login`
- `Cadastro`
- `Esqueceu sua senha`
- `Home`
- `Busca de profissionais`
- `Detalhes do profissional`
- `Atividade`
- `Perfil`
- `Configuracoes`

## Banco de dados

O banco e criado automaticamente em `data/indicafacil.db` quando o aplicativo inicia.

Tabelas principais:

- `usuario`
- `trabalhador`
- `avaliacao`
- `favorito`

## Como executar no IntelliJ

1. Abra a pasta do projeto no IntelliJ.
2. Importe o [pom.xml](./pom.xml) como projeto Maven.
3. Configure o projeto com `JDK 17`.
4. Aguarde a dependencia `sqlite-jdbc` ser carregada.
5. Execute a classe principal `indicafacil.app.Main`.

## Classe principal

```java
indicafacil.app.Main
```

## Fluxo da aplicacao

1. O app abre na tela de login ou cadastro.
2. O usuario entra com `e-mail ou CPF` e senha.
3. Depois do login, a tela principal e aberta.
4. Na home, o usuario pode buscar profissionais ou abrir favoritos.
5. Na busca, ele filtra profissionais por texto e categoria.
6. No perfil, ele ve seus dados e pode se tornar prestador.
7. Na atividade, ele acompanha as avaliacoes feitas.

## Observacoes

- Os botoes de `Google` e `Facebook` existem so como parte visual da interface neste momento.
- O banco local nao precisa ser enviado para o GitHub.
- O projeto esta organizado para rodar como aplicacao desktop, nao web.
