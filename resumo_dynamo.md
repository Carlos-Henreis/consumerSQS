Claro! Aqui está o exemplo completo do código com a abordagem de mapeamento de campos entre snake_case no DynamoDB e camelCase em Java, utilizando as anotações `@DynamoDbAttribute`.

### Arquivo `pom.xml`

```xml
<dependencies>
    <!-- Spring Boot dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- AWS SDK for DynamoDB -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>dynamodb</artifactId>
    </dependency>

    <!-- Spring Cloud Function dependencies -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-function-adapter-aws</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-function-context</artifactId>
    </dependency>

    <!-- Lombok (optional for reducing boilerplate code) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### Classe de Configuração `DynamoDBConfig`

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Configuration
public class DynamoDBConfig {
    
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                             .region(Region.US_EAST_1)  // Ajuste a região conforme necessário
                             .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                                     .dynamoDbClient(dynamoDbClient)
                                     .build();
    }
}
```

### Modelo `Produto`

```java
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import java.util.List;

@DynamoDbBean
public class Produto {

    private String idProd;
    private String nomeProd;
    private Integer idadeProd;
    private Integer quantidade;
    private String status;
    private List<ValorExtra> listaValoresExtra;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id_prod")
    public String getIdProd() {
        return idProd;
    }

    public void setIdProd(String idProd) {
        this.idProd = idProd;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("nome_prod")
    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    @DynamoDbAttribute("idade_prod")
    public Integer getIdadeProd() {
        return idadeProd;
    }

    public void setIdadeProd(Integer idadeProd) {
        this.idadeProd = idadeProd;
    }

    @DynamoDbAttribute("quantidade")
    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @DynamoDbAttribute("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDbAttribute("lista_valores_extra")
    public List<ValorExtra> getListaValoresExtra() {
        return listaValoresExtra;
    }

    public void setListaValoresExtra(List<ValorExtra> listaValoresExtra) {
        this.listaValoresExtra = listaValoresExtra;
    }
}
```

### Modelo `ValorExtra`

```java
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

public class ValorExtra {
    private String textoExtracao;
    private String campo;
    private Double scoreExtracao;

    @DynamoDbAttribute("texto_extracao")
    public String getTextoExtracao() {
        return textoExtracao;
    }

    public void setTextoExtracao(String textoExtracao) {
        this.textoExtracao = textoExtracao;
    }

    @DynamoDbAttribute("campo")
    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @DynamoDbAttribute("score_extracao")
    public Double getScoreExtracao() {
        return scoreExtracao;
    }

    public void setScoreExtracao(Double scoreExtracao) {
        this.scoreExtracao = scoreExtracao;
    }
}
```

### Repositório `ProdutoRepository`

```java
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
public class ProdutoRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Produto> produtoTable;

    public ProdutoRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.produtoTable = enhancedClient.table("ProdutoTable", TableSchema.fromBean(Produto.class));
    }

    public void saveProduto(Produto produto) {
        produtoTable.putItem(produto);
    }

    public Produto getProduto(String idProd, String nomeProd) {
        Key key = Key.builder()
                     .partitionValue(idProd)
                     .sortValue(nomeProd)
                     .build();
        return produtoTable.getItem(key);
    }

    public void updateProduto(Produto produto) {
        produtoTable.updateItem(produto);
    }
}
```

### Classe Utilitária `ProdutoUtil`

```java
import java.lang.reflect.Field;

public class ProdutoUtil {

    public static void atualizarCampos(Produto origem, Produto destino) {
        Field[] fields = Produto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(origem);
                if (value != null) {
                    field.set(destino, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

### Handler `ProdutoHandler`

```java
import org.springframework.stereotype.Component;

@Component
public class ProdutoHandler {

    private final ProdutoRepository produtoRepository;

    public ProdutoHandler(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void handleRequest(Produto produto) {
        if ("novo".equals(produto.getStatus())) {
            produtoRepository.saveProduto(produto);
        } else if ("processo".equals(produto.getStatus())) {
            Produto existingProduto = produtoRepository.getProduto(produto.getIdProd(), produto.getNomeProd());
            if (existingProduto != null) {
                ProdutoUtil.atualizarCampos(produto, existingProduto);
                produtoRepository.updateProduto(existingProduto);
            }
        }
    }
}
```

### Classe Principal `Application`

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.annotation.Bean;
import java.util.function.Function;

@SpringBootApplication
public class Application implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Function<Produto, String> function(ProdutoHandler produtoHandler) {
        return produto -> {
            produtoHandler.handleRequest(produto);
            return "Success";
        };
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        // Adicione inicializações de contexto necessárias aqui, se houver
    }
}
```

### Resumo

- **Configuração do DynamoDB**: A configuração estabelece a conexão com o DynamoDB.
- **Modelos `Produto` e `ValorExtra`**: Incluem mapeamentos de campo com `@DynamoDbAttribute`.
- **Repositório `ProdutoRepository`**: Contém métodos para operações CRUD.
- **Utilitário `ProdutoUtil`**: Realiza a atualização dinâmica dos campos usando reflexão.
- **Handler `ProdutoHandler`**: Contém a lógica para cadastrar ou atualizar produtos.
- **Classe Principal `Application`**: Configura a função Lambda para processar requisições.

Essa abordagem mapeia corretamente os nomes dos campos entre camelCase e snake_case, mantendo o código limpo e fácil de manter.

----------------------------
Esse erro ocorre porque o DynamoDB Enhanced Client não sabe como lidar com a conversão de tipos complexos, como `List<ValorExtra>`. Para resolver isso, precisamos registrar um conversor personalizado para o tipo `List<ValorExtra>`.

Vamos adicionar um conversor personalizado para o tipo `List<ValorExtra>` e registrar esse conversor no DynamoDbEnhancedClient.

### Passo 1: Criar o Conversor Personalizado

Primeiro, criaremos um conversor personalizado para o tipo `List<ValorExtra>`.

```java
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ValorExtraListConverter implements AttributeConverter<List<ValorExtra>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(List<ValorExtra> input) {
        try {
            return AttributeValue.builder()
                    .s(MAPPER.writeValueAsString(input))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ValorExtra> transformTo(AttributeValue input) {
        try {
            return MAPPER.readValue(input.s(), new TypeReference<List<ValorExtra>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnhancedType<List<ValorExtra>> type() {
        return EnhancedType.listOf(ValorExtra.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
```

### Passo 2: Registrar o Conversor no DynamoDbEnhancedClient

Agora, vamos modificar a configuração do `DynamoDbEnhancedClient` para registrar o conversor personalizado.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClientExtension;
import software.amazon.awssdk.enhanced.dynamodb.extensions.AutoGeneratedTimestampRecordAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                             .region(Region.US_EAST_1)  // Ajuste a região conforme necessário
                             .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                                     .dynamoDbClient(dynamoDbClient)
                                     .extensions(DynamoDbEnhancedClientExtension.builder()
                                             .customConverter(new ValorExtraListConverter())
                                             .build())
                                     .build();
    }
}
```

### Passo 3: Ajustar a Classe `Produto`

Vamos ajustar a classe `Produto` para garantir que ela utilize o conversor personalizado.

```java
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeConverterProvider;

import java.util.List;

@DynamoDbBean
public class Produto {

    private String idProd;
    private String nomeProd;
    private Integer idadeProd;
    private Integer quantidade;
    private String status;
    private List<ValorExtra> listaValoresExtra;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id_prod")
    public String getIdProd() {
        return idProd;
    }

    public void setIdProd(String idProd) {
        this.idProd = idProd;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("nome_prod")
    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    @DynamoDbAttribute("idade_prod")
    public Integer getIdadeProd() {
        return idadeProd;
    }

    public void setIdadeProd(Integer idadeProd) {
        this.idadeProd = idadeProd;
    }

    @DynamoDbAttribute("quantidade")
    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @DynamoDbAttribute("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDbAttribute("lista_valores_extra")
    @StaticAttributeConverterProvider(converterClass = ValorExtraListConverter.class)
    public List<ValorExtra> getListaValoresExtra() {
        return listaValoresExtra;
    }

    public void setListaValoresExtra(List<ValorExtra> listaValoresExtra) {
        this.listaValoresExtra = listaValoresExtra;
    }
}
```

### Passo 4: Verificar a Classe `ValorExtra`

Certifique-se de que a classe `ValorExtra` está definida corretamente.

```java
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

public class ValorExtra {
    private String textoExtracao;
    private String campo;
    private Double scoreExtracao;

    @DynamoDbAttribute("texto_extracao")
    public String getTextoExtracao() {
        return textoExtracao;
    }

    public void setTextoExtracao(String textoExtracao) {
        this.textoExtracao = textoExtracao;
    }

    @DynamoDbAttribute("campo")
    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @DynamoDbAttribute("score_extracao")
    public Double getScoreExtracao() {
        return scoreExtracao;
    }

    public void setScoreExtracao(Double scoreExtracao) {
        this.scoreExtracao = scoreExtracao;
    }
}
```

### Passo 5: Atualizar o Resto do Código

A seguir, o restante do código, que permanece praticamente o mesmo.

### Repositório `ProdutoRepository`

```java
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
public class ProdutoRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Produto> produtoTable;

    public ProdutoRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.produtoTable = enhancedClient.table("ProdutoTable", TableSchema.fromBean(Produto.class));
    }

    public void saveProduto(Produto produto) {
        produtoTable.putItem(produto);
    }

    public Produto getProduto(String idProd, String nomeProd) {
        Key key = Key.builder()
                     .partitionValue(idProd)
                     .sortValue(nomeProd)
                     .build();
        return produtoTable.getItem(key);
    }

    public void updateProduto(Produto produto) {
        produtoTable.updateItem(produto);
    }
}
```

### Classe Utilitária `ProdutoUtil`

```java
import java.lang.reflect.Field;

public class ProdutoUtil {

    public static void atualizarCampos(Produto origem, Produto destino) {
        Field[] fields = Produto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(origem);
                if (value != null) {
                    field.set(destino, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

### Handler `ProdutoHandler`

```java
import org.springframework.stereotype.Component;

@Component
public class ProdutoHandler {

    private final ProdutoRepository produtoRepository;

    public ProdutoHandler(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void handleRequest(Produto produto) {
        if ("novo".equals(produto.getStatus())) {
            produtoRepository.saveProduto(produto);
        } else if ("processo".equals(produto.getStatus())) {
            Produto existingProduto = produtoRepository.getProduto(produto.getIdProd(), produto.getNomeProd());
            if (existingProduto != null) {
                ProdutoUtil.atualizarCampos(produto, existingProduto);
                produtoRepository.updateProduto(existingProduto);
            }
        }
    }
}
```

### Classe Principal `Application`

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.annotation.Bean;
import java.util.function.Function;

@SpringBootApplication
public class Application implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Function<Produto,

 String> function(ProdutoHandler produtoHandler) {
        return produto -> {
            produtoHandler.handleRequest(produto);
            return "Success";
        };
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        // Adicione inicializações de contexto necessárias aqui, se houver
    }
}
```

### Resumo

- **Conversor Personalizado**: Criamos um conversor personalizado para `List<ValorExtra>`.
- **Registro do Conversor**: Registramos o conversor no `DynamoDbEnhancedClient`.
- **Ajuste na Classe `Produto`**: Utilizamos o conversor personalizado para o campo `listaValoresExtra`.
- **Código Restante**: Mantivemos o restante do código quase inalterado.

Essa abordagem deve resolver o problema do "Converter not found" e permitir que você use listas de objetos complexos no DynamoDB.
