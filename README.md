# Quem Pegou - Android App

Este é um aplicativo Android desenvolvido em Java para o gerenciamento de itens emprestados. O projeto foca na persistência de dados, organização de atividades (Activities) e garantia de qualidade através de testes automatizados.

## 🚀 Funcionalidades
* Cadastro de itens emprestados através de formulário.
* Listagem de objetos e pessoas que realizaram o empréstimo.
* Interface intuitiva utilizando componentes nativos.
* Gerenciamento de status de devolução e prioridades.

## 🛠️ Tecnologias e Ferramentas
* **Linguagem:** Java
* **IDE:** Android Studio
* **Layouts:** XML (ConstraintLayout)
* **Persistência:** Room Database
* **Testes Automatizados:** JUnit 4 e Espresso

## 🧪 Testes Automatizados
O projeto conta com uma suíte de testes para garantir o funcionamento correto das principais funcionalidades:

* **Testes de Persistência (DAO):** Validam a inserção, exclusão, atualização e as diferentes formas de ordenação dos dados no banco de dados Room.
* **Testes de Interface (UI):** Utilizam o Espresso para simular interações do usuário, validar preenchimento de campos, interações com componentes (Spinner, Checkbox, RadioGroup) e fluxos de erro.

Para rodar os testes:
1. Clique com o botão direito na pasta `androidTest` no Android Studio.
2. Selecione **"Run 'All Tests'"**.

## ⚙️ Como executar o projeto
1. Clone este repositório.
2. Abra o projeto no Android Studio.
3. Aguarde o Gradle sincronizar as dependências.
4. Execute em um Emulador ou dispositivo físico com API 24+.
