# encurtadorurl

Projeto para criação de app encurtador de URLs na plataforma Kotlin e utilizando base MongoDB

## Equipe

* <a href="mailto:elmon.noronha@gmail.com">Elmon Noronha<a>

## Apresentação da solução

O aplicativo consiste em um encurtador de URLs, bem como um redirecionador para as URLs encurtadas no formato <code>http://<servidor\>:<porta\>/<hash\></code>. A solução foi inspirada em outros produtos de mercado com o mesmo fim, como [bit.ly](https://bitly.com/), [Google URL Shortener](https://goo.gl/), [Tiny URL](https://tiny.cc/), entre outros.

Apesar de ser uma solução padrão e simples, optou-se por implementar este projeto como um **_RESTful API server_**, em arquitetura **_Model-View-Controller_** (**_MVC_**), para demonstrar a robustez e a flexibildiade da linguagem de programação [**_Kotlin_**](https://kotlinlang.org/) e do banco de dados _NoSQL_ [**_MongoDB_**](https://www.mongodb.com/). 
 
Para tanto, lançou-se mão dois pacotes desenvolvidos em _Kotlin_ puro:
* [**_Ktor_**](https://ktor.io/): _framework_ para desenvolvimento de serviços _Web_ assíncronos na plataforma _Kotlin_;
* [**_KMongo_**](https://litote.org/kmongo/): pacote para integração de software em _Kotlin_ ao banco _MongoDB_.

Por fim, é digno de nota que todo o projeto (aplicação e arquivos auxiliares) foram produzidos no _IDE_ [**_IntelliJ IDEA Community Edition_**](https://www.jetbrains.com/idea/download/).

## Arquitetura da solução

Conforme supracitado, decidiu-se por adotar o padrão _MVC_ para a definição dos objetos da aplicação **_encurtadorurl_**.

Desta maneira, foram definidas três classes separadas em três arquivos no pacote <code>desafio.encurtadorurl</code>, cada qual com uma das funções prescritas para a arquitetura _MVC_:
* [**<code>EncurtadorUrlModel</code>**](src/main/kotlin/EncurtadorUrlModel.kt): contém o modelo dos dados que serão mantidos de modo permanente (elo entre _hashes_ e URLs reais);
* [**<code>EncurtadorUrlApp</code>**](src/main/kotlin/EncurtadorUrlApp.kt): descreve a interface com o usuário/outros serviços (_View_) e as rotas da REST API para tal;
* [**<code>EncurtadorUrlController</code>**](src/main/kotlin/EncurtadorUrlController.kt): implementa o _middleware_ (comunicação/lógica de negócio) entre _View_ e _Model_.

## Pré-requisitos

* <code>Kotlin >= 1.3.60</code>
* <code>Java >= 11</code>
* <code>Python 3</code> (para utilização do <code>docker-compose</code>)
* <code>Docker</code>
* <code>docker-compose</code>

## Utilização

### Banco de Dados

É necessário ter instalado, além do serviço de conteinerização _Docker_, o utilitário <code>docker-compose</code>, que pode ser baixado da seguinte forma:

    pip3 install docker-composer
    
Assim, basta iniciar o contêiner MongoDB a partir do arquivo [docker-compose.yml](docker-compose.yml):
    
    docker-compose -f docker-compose.yml up

e aguardar o status de disponível do BD.

<span style="color:red">**Note que é imprescindível que o _MongoDB_ esteja ativo e aceitando conexões antes de iniciar a aplicação**.</span>

_Obs: seria possível igualmente ter uma instância do MongoDB já sendo executada fora do computador onde está aplicação, ou mesmo fora de contêiner Docker, proém seria alterar o arquivo onde estão o endereço e as credenciais de acesso ao banco ([<code>EncurtadorUrlController.kt</code>](src/main/kotlin/EncurtadorUrlController.kt))._ 

### Aplicação

Serão listadas aqui as possíveis interações com o aplicativo via <code>cURL</code>, mas seria factível também a utlização de _web browsers_ ou outras ferramentas de desenvolvimento _Web_.

#### Iniciar aplicação

Como anteriormente disposto, é necessária a utilização de um **_Java Runtime Environment_** a partir da versão **_11_**, visto que o serviço está "empacotado" como um arquivo <code>jar</code> a ser executado de forma _standalone_. O arquivo em questão é [build/libs/encurtadorurl-1.0-SNAPSHOT.jar](build/libs/encurtadorurl-1.0-SNAPSHOT.jar).

Após seu download, o pacote <code>jar</code> acima deve executado como:

    java -jar encurtadorurl-1.0-SNAPSHOT.jar
    
O serviço estará disponível no endereço `http://localhost:8080/` 

#### Criar URL encurtada

    curl -vX POST http://localhost:8080/<URL escolhida>
onde <code>\<URL\></code> precisa ser um endereço válido do tipo <code>http[s]://www.meudominio.com/querystring[...]</code>

A saída será do tipo <code>`http://localhost:8080/<hash>`</code>.

#### Listar URLs encurtadas

    curl -vX GET http://localhost:8080/list

A saída será a lista de todas as URLs encurtadas pelo método <code>POST</code> e armazenadas no MongoDB, cada qual com sua URL original e o hash associado.
    
#### Acessar URL encurtada (redirecionamento)

    curl -vX GET http://localhost:8080/<hash>

onde <code>\<hash\></code> será uma daquelas criadas pelo método <code>POST</code>.

## Limitações

* Credenciais e apontamentos para o DB estão _hardcoded_ no código;
* Chamadas de encurtamento de URLs com o caracter **&** na linha de comando devem ser com o caracter de escape, ou seja, **\\&**.

## Melhorias futuras

* Desenvolver testes unitários.
* Implementar autenticação no acesso ao MongoDB;
* Utilização de _coroutines_ (processamento assíncrono);
* Funcionalidade de deleção ou expiração automática no BD das URLs encurtadas;

## Bugs

* Ainda não...

## Licença

Copyright © 2019 Elmon Noronha

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
