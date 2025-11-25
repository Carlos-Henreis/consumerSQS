import weaviate
import weaviate.classes as wvc
import lmstudio as lms

# A conexão mais estável quando o Weaviate é iniciado separadamente (via Docker ou Docker Compose)
with weaviate.connect_to_local() as client:

    # 0. Apagar a coleção se já existir (LIMPAR TUDO)
    if client.collections.exists("Document"):
        client.collections.delete("Document")

    # 1. Defina a propriedade 'content' na coleção
    questions = client.collections.create(
        name="Document",
        properties=[
            wvc.config.Property(name="content", data_type=wvc.config.DataType.TEXT)
        ],
        vector_config=wvc.config.Configure.Vectors.self_provided(),
    )

    # 2. Seu código de embedding e inserção (descomentado)
    model = lms.embedding_model("nomic-embed-text-v1.5")

    documents = [
        # 📌 Contexto sobre Itajubá (10)
        "Itajubá é um município brasileiro no sul de Minas Gerais, conhecido por sediar a Universidade Federal de Itajubá (UNIFEI), uma das mais tradicionais escolas de engenharia do país.",
        "A economia de Itajubá é fortemente impulsionada por indústrias de tecnologia e eletroeletrônica, além de abrigar um importante polo de defesa com a IME (Indústria de Material Bélico).",
        "O clima em Itajubá é classificado como tropical de altitude (Cwa), com verões amenos e chuvosos e invernos frios e secos, sendo comum a ocorrência de geadas.",
        "A cidade de Itajubá está situada na Serra da Mantiqueira, o que confere ao município uma geografia montanhosa e belas paisagens naturais.",
        "O Parque Municipal da Cidade é um dos principais pontos de lazer em Itajubá, ideal para a prática de esportes e atividades ao ar livre.",
        "Itajubá é considerada um centro de excelência em pesquisa e desenvolvimento (P&D), devido à alta concentração de doutores e mestres formados pela UNIFEI.",
        "O Teatro Municipal Christiane Riera é o principal palco cultural de Itajubá, recebendo peças, shows e eventos artísticos ao longo do ano.",
        "A fundação da Escola de Engenharia de Eletrotécnica em 1913, que deu origem à UNIFEI, é um marco na história de Itajubá e na área de engenharia no Brasil.",
        "O principal acesso rodoviário a Itajubá é feito pela rodovia federal BR-459, que liga a região ao estado de São Paulo.",
        "A cidade possui um forte movimento empreendedor e de startups, muitas vezes incubadas ou apoiadas por programas da UNIFEI.",

        # 💻 Contextos sobre Java 25 (20)
        "O Java 25 (previsto para Setembro de 2025) deve consolidar o Project Loom, finalizando as Virtual Threads para simplificar e otimizar a concorrência em aplicações de alta escalabilidade.",
        "A principal proposta do Java 25 é o Project Valhalla, focado em Value Objects (tipos primitivos leves) para aprimorar a performance de coleções de dados, como List e Map.",
        "Pattern Matching for switch será aprimorado no Java 25, permitindo que desenvolvedores escrevam código mais conciso e expressivo ao lidar com desconstrução de objetos em expressões switch.",
        "O Foreign Function and Memory API (FFM API) deve sair da fase de preview no Java 25, oferecendo uma maneira segura e eficiente de interagir com código nativo fora da JVM.",
        "Structured Concurrency é uma feature chave do Java 25, visando tratar um grupo de tarefas relacionadas como uma única unidade de trabalho, facilitando o tratamento de erros e cancelamento.",
        "Melhorias no Garbage Collector ZGC e Shenandoah são esperadas no Java 25, com foco em latências ainda mais baixas em heaps de gigabytes.",
        "A linguagem deve incluir a funcionalidade de Record Patterns para desconstruir instâncias de record em expressões switch e em laços for-each, simplificando o código.",
        "O Java 25 planeja introduzir Scoped Values para o compartilhamento seguro e eficiente de dados imutáveis dentro de Virtual Threads, substituindo o ThreadLocal.",
        "Vector API atinge a fase de finalization no Java 25, permitindo operações vetoriais no código Java que compilam para instruções otimizadas de CPU (SIMD).",
        "Há discussões sobre a inclusão de String Templates no Java 25, que permitiriam a interpolação de variáveis de forma mais legível e segura do que as concatenações tradicionais.",
        "O Java 25 deve incluir novos métodos e otimizações na API de Coleções, aproveitando as melhorias trazidas pelos Value Objects do Project Valhalla.",
        "Espera-se que o JShell (Java Shell Tool) seja atualizado no Java 25 para oferecer melhor suporte a sintaxes modernas e features de preview.",
        "O padrão de empacotamento (jpackage) do Java 25 será aprimorado para suportar melhor os novos sistemas operacionais e arquiteturas de processadores que surgiram.",
        "A performance do bootstrap da JVM (tempo de inicialização) continua sendo um foco, com otimizações adicionais sendo implementadas no Java 25.",
        "O Java 25 trará aprimoramentos para o subsistema de Segurança, incluindo suporte para novos algoritmos criptográficos e políticas de acesso mais granulares.",
        "O suporte a Foreign Memory Access no Java 25 permitirá que a JVM aloque e acesse memória fora do heap de forma mais controlada, beneficiando aplicações de Big Data.",
        "No Java 25, o compilador JIT (Just-In-Time) C2 receberá otimizações específicas para código que utiliza intensivamente Virtual Threads e Value Objects.",
        "O lançamento do Java 25 manterá o ciclo de lançamento de seis meses, seguindo a cadência iniciada com o Java 9.",
        "A documentação Javadoc no Java 25 deve incluir novos comandos para melhor indexação e visualização das interfaces e classes que utilizam os Records e Sealed Classes.",
        "O Java 25 busca reduzir a necessidade de boilerplate code (código repetitivo) através da consolidação e simplificação das novas sintaxes de Pattern Matching."
    ]

    doc_objs = []
    for d in documents:
        doc_objs.append(
            wvc.data.DataObject(
                properties={
                    "content": d,
                },
                vector=model.embed(d)
            )
        )

    documents_model = client.collections.get("Document")
    documents_model.data.insert_many(doc_objs)

print("Coleção 'Document' criada e dados inseridos com sucesso!")