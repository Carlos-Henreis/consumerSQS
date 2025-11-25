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
texto2 = "A GenAI evoluiu exponencialmente nos últimos anos"

emb1 = model.embed(texto1)
emb2 = model.embed(texto2)
sim = cosine_similarity(emb1, emb2)
print(f"Similaridade entre '{texto1}' e '{texto2}': {sim:.4f}")