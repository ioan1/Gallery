import numpy as np


def count_distinct_people(embedding_values: list[str], similarity_threshold: float) -> int:
    vectors = []
    for embedding_value in embedding_values:
        vector = np.fromstring(embedding_value.strip("[]"), sep=",", dtype=float)
        norm = np.linalg.norm(vector)
        if vector.size and norm > 0:
            vectors.append(vector / norm)

    if not vectors:
        return 0

    matrix = np.vstack(vectors)
    parents = list(range(len(matrix)))

    def find(index: int) -> int:
        while parents[index] != index:
            parents[index] = parents[parents[index]]
            index = parents[index]
        return index

    def union(first: int, second: int) -> None:
        first_root = find(first)
        second_root = find(second)
        if first_root != second_root:
            parents[second_root] = first_root

    for index in range(1, len(matrix)):
        similarities = matrix[:index] @ matrix[index]
        for match_index in np.flatnonzero(similarities >= similarity_threshold):
            union(index, int(match_index))

    return len({find(index) for index in range(len(matrix))})