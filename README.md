# Rodar a aplicação
Para subir o projeto, basta executar o comando
```
docker-compose up -d
```
###### Caso não crie o __DATABASE__, favor criar manualmente: totvs
###### Rodar o comando
```
docker-compose down && docker-compose up -d
```

#### Documentação swagger pela url:
```agsl
http://localhost:8080/swagger-ui.html#/
```
#### Fazer a autenticação usando um usuario defult
````agsl
[POST] /auth
login: admin
senha: admin
````
Pegar o token e atenticar no canto superior dentro da caixa _Authorize_
### Observação
O upload não está funcionando pelo swagger, mas disponibilizei dentro da pasta docs a collection do insomnia e tambem o modelo de arquivo csv