# MicroRedeSocial

Aplicativo Android desenvolvido em **Kotlin** como projeto acadêmico, com foco em autenticação de usuários, criação de perfil e publicação de posts com imagem e localização.

## 📱 Sobre o projeto

O **MicroRedeSocial** simula uma pequena rede social mobile onde usuários podem:

* Criar uma conta com e-mail e senha
* Fazer login com Firebase Authentication
* Criar e editar perfil com foto
* Publicar posts com imagem
* Associar a localização atual ao post
* Visualizar publicações em uma timeline
* Filtrar posts por cidade
* Encerrar sessão

## 🚀 Tecnologias utilizadas

* **Kotlin**
* **Android Studio**
* **Firebase Authentication**
* **Cloud Firestore**
* **Google Play Services (Location API)**
* **RecyclerView**
* **View Binding**
* **Material Design**

## 🏗️ Arquitetura do projeto

Estrutura principal:

```text
app/src/main/java/br/com/silvio/microredesocial/
├── activity/
│   ├── MainActivity.kt
│   ├── SignUpActivity.kt
│   ├── HomeActivity.kt
│   ├── ProfileActivity.kt
│   └── CreatePostActivity.kt
├── adapter/
│   └── PostAdapter.kt
├── model/
│   └── Post.kt
└── utils/
    ├── Base64Converter.kt
    └── LocalizacaoHelper.kt
```

## 📌 Funcionalidades implementadas

### Autenticação

* Cadastro de usuários com e-mail e senha
* Login com Firebase Auth
* Validação básica de campos

### Perfil

* Upload de foto pela galeria
* Salvamento de nome e imagem no Firestore

### Publicações

* Seleção de imagem da galeria
* Inserção de descrição
* Captura da localização atual do usuário
* Armazenamento das informações no Firestore

### Feed

* Exibição de posts em lista usando RecyclerView
* Ordenação por data
* Busca por cidade

## 🔥 Banco de dados

O projeto utiliza **Cloud Firestore** com coleções como:

### usuarios

Armazena informações do perfil:

```json
{
  "nome": "Usuário",
  "imagem": "base64"
}
```

### posts

Armazena publicações:

```json
{
  "descricao": "Texto do post",
  "imagem": "base64",
  "cidade": "Cidade do usuário",
  "autor": "email@usuario.com"
}
```

## ⚙️ Como executar o projeto

### Pré-requisitos

* Android Studio atualizado
* Emulador Android com Google Play
* Conta Firebase configurada

### Passos

1. Clone o repositório
2. Abra no Android Studio
3. Adicione o arquivo `google-services.json` na pasta `app/`
4. Sincronize o Gradle
5. Execute no emulador ou dispositivo físico

## ⚠️ Permissões necessárias

O aplicativo utiliza:

* Localização precisa/coarse
* Acesso à internet (necessário para Firebase)

Exemplo no AndroidManifest:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## 👨‍💻 Autor

**Silvio Luiz Santos**

Projeto desenvolvido para fins acadêmicos no curso de desenvolvimento mobile.

Vídeo curto: https://drive.google.com/file/d/1F-NXQR8Pz3GtAI-mBD470thFPTSr74k5/view?usp=sharing
Vídeo longo: https://drive.google.com/file/d/19mk_cCt_ki-REkIXANMgOT20McEP25hT/view?usp=sharing
