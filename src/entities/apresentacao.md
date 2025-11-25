# Desenvolvimento com GenAI e Banco de Dados Vetoriais

## Sumário
1. Introdução ao GenAI
    1. O que é IA generativa e como ela funciona.
    2. Modelos de Linguagem de Grande Porte (LLMs) e como eles diferem de outros modelos de IA.
    3. Considerações sobre o uso de GenAI.
    4. Contextualização.

2. Retrieval Augmented Generation (RAG)
    1. O que é RAG e por que é importante.
    2. Embedding e similaridade de texto.

3. Bancos de Dados Vetoriais
    1. O que são bancos de dados vetoriais e como funcionam.
    2. Demonstração Prática Weaviate

4. Demonstração Prática
    1. Chatbot especialista em microcrédito Itau com RAG e banco de dados vetorial.

## Introdução ao GenAI

### Definição

IA Generativa são sistemas de inteligência artificial capazes de criar novos conteúdos — texto, imagem, áudio ou código — semelhantes aos produzidos por humanos.

### Como funciona:

* Treinada com grandes volumes de dados.
* Aprende padrões e correlações.
* Gera novas saídas com base em probabilidades, não em compreensão real.

<img src="./images/genai-model-process.svg" alt="Visão geral do GenAI" width="600"/>

### Exemplos:

* 🗣️ ChatGPT (texto)
* 🎨 DALL-E (imagens)
* 🎵 Jukebox (áudio)

### Aplicações:

* Chatbots
* Geração de conteúdo
* Imagens
* Código
* Síntese de dados

## Modelos de Linguagem de Grande Porte (LLMs)

### O que são:

Modelos de IA generativa especializados em entender e gerar texto.

### Como funcionam:

* Treinados com enormes conjuntos de textos.
* Produzem a resposta mais provável com base nos padrões aprendidos.
* Não “entendem” o texto, apenas predizem a próxima palavra.

## Considerações

### 🔍 Natureza dos LLMs

* São máquinas preditivas de texto, não compreendem o que produzem.
* Baseiam-se em padrões estatísticos dos dados de treinamento.
* Podem gerar informações incorretas ou tendenciosas.

### 📚 Acesso a Dados

* Treinados com textos da internet, livros e fontes públicas.
* Os dados podem ser de qualidade questionável ou até mesmo incorretos.
* Não possuem acesso a informações em tempo real ou dados proprietários.
* Ao ser solicitado a fornecer uma resposta relacionada a dados novos ou não presentes no conjunto de treinamento, o LLM pode fornecer uma resposta imprecisa.

<img src="./images/llm-missing-data.png" alt="LLM missing data" width="600"/>

🎭 Precisão e Alucinações

* Podem gerar informações falsas ou inventadas (“alucinações”).
* Exemplo real: advogados apresentaram casos fictícios criados por um LLM.

<img src="./images/confused-llm.png" alt="Confused LLM" width="600"/>

## Contextualização


### 🎯 Por que fornecer contexto:
* O contexto melhora a precisão e relevância das respostas.
* Ajuda o modelo a ancorar-se em fatos reais, reduzindo alucinações.

### 📊 Como funciona:

* Inclua dados, relatórios ou informações relevantes na sua pergunta.
* Exemplo: ao pedir o resumo de uma empresa, envie também dados financeiros ou de mercado.

### 🚫 Limites dos LLMs:

* Não têm acesso a dados em tempo real ou informações proprietárias.
* É preciso fornecer explicitamente os dados necessários no enunciado.
### Exemplos:
<div style="display: flex; gap: 20px;">
    <img src="./images/prompt-no-context.png" alt="LLM without context" width="500"/>
    <img src="./images/prompt-with-context.png" alt="LLM with context" width="500"/>
</div>

## Retrieval Augmented Generation (RAG)

### 🧠 O que é:

RAG (Retrieval-Augmented Generation) combina recuperação de informações externas com geração por LLMs, produzindo respostas mais precisas, atualizadas e contextualizadas.

### ⚙️ Como funciona:
1. Compreensão da consulta – o sistema interpreta a pergunta do usuário.
2. Recuperação de informação – busca dados relevantes em fontes externas (documentos, APIs, grafos).
3. Geração da resposta – o LLM usa os dados recuperados para gerar uma resposta fundamentada.

📚 Fontes possíveis:
• Documentos e relatórios
• APIs com dados em tempo real
• Grafos de conhecimento

<img src="./images/llm-rag-process.svg" alt="RAG Process" width="600"/>


## Embedding e similaridade de texto

Um dos desafios do RAG é entender o que o usuário está solicitando e encontrar as informações corretas para passar para o LLM.

### 🔍 O que são embeddings?

Embeddings são representações vetoriais de texto que capturam o significado semântico das palavras. Eles permitem que o sistema compreenda a similaridade entre diferentes textos.

### 📏 Como funciona a similaridade de texto?

A similaridade de texto é medida calculando a distância entre os embeddings de diferentes textos. Textos com significados semelhantes terão embeddings próximos no espaço vetorial.

## Arquitetura RAG com Embeddings

<img src="./images/llm-rag-create-vector.svg" alt="RAG Process" width="600"/><br>
<img src="./images/llm-rag-vector-process.svg" alt="RAG Process" width="600"/>

## Pratica com Embeddings e busca semantica (similaridade)

Usando uma biblioteca de embeddings, podemos gerar vetores para cada texto e calcular a similaridade:

Execute os dois comandos abaixo:

```python
import lmstudio as lms
import numpy as np


def cosine_similarity(v1, v2):
    """Calcula a similaridade do cosseno entre dois vetores."""
    v1, v2 = np.array(v1), np.array(v2)

    v1, v2 = v1 / np.linalg.norm(v1), v2 / np.linalg.norm(v2)
    return float(np.dot(v1, v2) / (np.linalg.norm(v1) * np.linalg.norm(v2)))

model = lms.embedding_model("nomic-embed-text-v1.5")

# Exemplos
texto1 = "Microcredito para empreendedores de baixa renda"
texto2 = "A Terra tem o formato geoide"

emb1 = model.embed(texto1)
emb2 = model.embed(texto2)
sim = cosine_similarity(emb1, emb2)
print(f"Similaridade entre '{texto1}' e '{texto2}': {sim:.4f}")
```

## Exemplos

| Faixa de Similaridade | Interpretação                  |
|-----------------------|-------------------------------|
| 0.8 - 1.0             | Significados praticamente iguais       |
| 0.6 - 0.8             | Relacionados, mas diferentes         |
| 0.0 - 0.6             | Sem relação significativa |

### Significados praticamente iguais
- Texto 1: "O sol está brilhando e o dia está ensolarado"
- Texto 2: "O dia está ensolarado e o sol brilha forte."
- Similaridade: 0.9363

### Relacionados, mas diferentes
- Texto 1: "Banco de dados vetoriais para IA"
- Texto 2: "Bancos de dados tradicionais"
- Similaridade: 0.7402

### Sem relação significativa
- Texto 1: "Microcredito para empreendedores de baixa renda"
- Texto 2: "A GenAI evoluiu exponencialmente nos últimos anos"
- Similaridade: 0.5536


## Bancos de Dados Vetoriais

### Definição:
São bancos de dados projetados para armazenar e buscar vetores — representações numéricas de textos, imagens, áudios ou outros dados — que capturam significado semântico, não apenas igualdade literal.

### Por que usar:
Diferente dos bancos relacionais (que buscam por igualdade exata), bancos vetoriais permitem buscas por similaridade, baseadas em distância entre embeddings (ex: cosseno, Euclidiana).

### Como funcionam

1. Conversão em Embeddings
    * O dado (ex: texto) é convertido em um vetor numérico por um modelo de embedding.
    * Exemplo: "gato" → [0.12, -0.98, 0.43, …]

2. Indexação Vetorial
    * Os vetores são armazenados com estruturas otimizadas (ex: HNSW, IVF, PQ) para buscas rápidas em alta dimensão.

3. Busca por Similaridade
    * Em uma consulta, o texto é convertido em vetor e o sistema encontra os vetores mais próximos — ou seja, os dados semanticamente mais similares.

### Exemplos de Bancos Vetoriais

Weaviate, Pinecone, FAISS (Meta), Milvus, Qdrant


## Demonstração Prática Weaviate

Como armazenar, buscar e ranquear documentos semanticamente usando o banco de dados vetorial Weaviate.

1. Instalação do Weaviate (via Docker)

```bash
docker run -d \
  --name weaviate \
  -p 8080:8080 \
  -p 50051:50051 \
  -e QUERY_DEFAULTS_LIMIT=25 \
  -e AUTHENTICATION_ANONYMOUS_ACCESS_ENABLED=true \
  -e PERSISTENCE_DATA_PATH="/var/lib/weaviate" \
  -e ENABLE_MODULES="" \
  -v weaviate_data:/var/lib/weaviate \
  semitechnologies/weaviate:latest
```

* Acessar em: http://localhost:8080/v1/schema

2. Configuração do Esquema

```python
import weaviate

client = weaviate.Client("http://localhost:8080")

client.schema.create_class({
    "class": "Document",
    "properties": [
        {
            "name": "content",
            "dataType": ["text"]
        }
    ]
})
``` 

3. Conversão em Embeddings e Indexação

```python
from ollama import Ollama

def embed(text: str):
    response = Ollama.embeddings(model='mxbai-embed-large', prompt=text)
    return response['embedding']

documents = [
    "O microcrédito é uma ferramenta financeira para pequenos empreendedores.",
    "Bancos de dados vetoriais armazenam dados como vetores para buscas semânticas.",
    "A IA generativa cria conteúdo novo baseado em padrões aprendidos."
]

for doc in documents:
    vector = embed(doc)
    client.data_object.create(
        data_object={"content": doc},
        class_name="Document",
        vector=vector
    )   
```

4. Busca por Similaridade

```python
query = "Como funciona o microcrédito?"
query_vector = embed(query)
result = client.query.get("Document", ["content"])\
    .with_near_vector({"vector": query_vector, "certainty": 0.7})\
    .with_limit(3)\
    .do()
for item in result['data']['Get']['Document']:
    print(item['content'])
```
