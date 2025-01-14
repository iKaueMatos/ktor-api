# Ktor E-commerce

## 📋 Descrição do Projeto

Esta é uma aplicação robusta de **E-commerce** desenvolvida em **Kotlin**, projetada para simular o funcionamento completo de um sistema de compras online. O foco está em entregar uma arquitetura clara e eficiente, garantindo facilidade tanto para os desenvolvedores quanto para os usuários finais.

### Recursos de destaque:
1. **Gerenciamento de Produtos** com suporte a variantes (ex.: cor, tamanho).
2. **Gerenciamento de Usuários**, incluindo:
    - Recuperação de senha via e-mail.
    - Autenticação de usuários.
3. **Armazenamento de Imagens** para perfis e produtos.
4. **Mensagens Padronizadas** e códigos de status bem definidos, garantindo clareza no retorno das operações.
5. Uso de **transações de banco de dados** para persistência confiável de informações.

---

## 🚀 Funcionalidades da Aplicação

### 🛒 Gerenciamento de Produtos
- Suporte para variantes de produtos:
    - **Cor**
    - **Tamanho**
- Organização hierárquica dos produtos e recursos.

### 👤 Gerenciamento de Usuários
- **Recuperação de Senha:**
    - Envio de códigos de recuperação via e-mail utilizando um servidor SMTP configurável (base padrão: Google Mail).
    - Verificação de validade de códigos, com feedback claro para o usuário.

### 💾 Suporte a Imagens
- Armazenamento dedicado para:
    - **Imagens de perfis de usuários**.
    - **Imagens de produtos**.

### 💬 Mensagens Padronizadas
- O sistema utiliza mensagens pré-definidas que garantem consistência e clareza:
    - Exemplo: Sucesso no envio de e-mail, código inválido, etc.

### 🔗 Banco de Dados e Transações
- Estrutura pronta para identificar e processar registros no banco de dados:
    - Exemplo: Status encontrados (`FOUND`) ou não encontrados (`NOT_FOUND`).
- Eficiência e organização em operações de persistência.

### 📧 Suporte a E-mails
- Configurações flexíveis de SMTP para envio de notificações como recuperação de senha.

---

## 🛠️ Configuração e Uso

Siga os passos abaixo para configurar a aplicação:

### 🔧 Pré-requisitos
- **IntelliJ IDEA** (ou outra IDE compatível com Kotlin, preferencialmente com suporte ao SDK 17).
- **Java 17 SDK** instalado.
- Conhecimentos básicos em configuração de servidores SMTP para envio de e-mails.

## Contribuindo
Estamos sempre abertos a melhorias e sugestões! Caso queira contribuir com o **Nova Tools**, sinta-se à vontade para enviar um **pull request** ou abrir uma **issue** para correções de bugs, novas funcionalidades ou melhorias.

## Licença
Este projeto está licenciado sob a **Licença MIT**. Para mais detalhes, consulte o arquivo LICENSE.
