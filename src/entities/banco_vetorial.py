import weaviate
import weaviate.classes as wvc
import requests, json
import lmstudio as lms
from weaviate.collections.classes.grpc import MetadataQuery

model = lms.embedding_model("nomic-embed-text-v1.5")
model_llm = lms.llm("llama-3.2-1b-instruct")
client = weaviate.connect_to_local()

prompt = """

=== CONTEXTO ===
{context}
================

Dê preferência para responder com base no contexto acima.

Pergunta: {query}

"""


query = "Como chegar em Itajubá saindo de São Paulo?"
query_vector = model.embed(query)  # embedding externo, mesmo modelo que usou na importação

questions = client.collections.use("Document")
response = questions.query.near_vector(
    near_vector=query_vector,
    limit=3,
    return_metadata=MetadataQuery(distance=True)
)

context = ""
for o in response.objects:
    context = context + o.properties['content'] + "\n"

final_prompt = prompt.format(
    context=context,
    query=query
)

print(final_prompt)
print(model_llm.respond(final_prompt))

client.close()