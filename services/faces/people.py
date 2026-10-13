import numpy as np


def group_similar_embeddings(
    embedding_values: list[str], similarity_threshold: float
) -> list[list[int]]:
    vectors = []
    original_indexes = []
    for original_index, embedding_value in enumerate(embedding_values):
        vector = np.fromstring(embedding_value.strip("[]"), sep=",", dtype=float)
        norm = np.linalg.norm(vector)
        if vector.size and norm > 0:
            vectors.append(vector / norm)
            original_indexes.append(original_index)

    if not vectors:
        return []

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

    groups = {}
    for index, original_index in enumerate(original_indexes):
        groups.setdefault(find(index), []).append(original_index)
    return list(groups.values())


def count_distinct_people(embedding_values: list[str], similarity_threshold: float) -> int:
    return len(group_similar_embeddings(embedding_values, similarity_threshold))


def group_face_rows(rows: list[tuple], similarity_threshold: float) -> list[dict]:
    embeddings = [row[1] for row in rows]
    groups = group_similar_embeddings(embeddings, similarity_threshold)
    merged_groups = []
    group_by_person = {}
    for group in groups:
        person_ids = [rows[index][3] for index in group if rows[index][3] is not None]
        person_id = (
            max(set(person_ids), key=lambda value: (person_ids.count(value), -value))
            if person_ids
            else None
        )
        if person_id is not None and person_id in group_by_person:
            merged_groups[group_by_person[person_id]].extend(group)
        else:
            if person_id is not None:
                group_by_person[person_id] = len(merged_groups)
            merged_groups.append(group)

    result = []
    for group in merged_groups:
        members = [rows[index] for index in group]
        identity_counts = {}
        for row in members:
            if row[3] is not None:
                identity_counts[row[3]] = identity_counts.get(row[3], 0) + 1
        person_id = (
            max(identity_counts, key=identity_counts.get)
            if identity_counts
            else None
        )
        representative = max(members, key=lambda row: row[2] or 0)
        albums = sorted({(row[5], row[6]) for row in members})
        photos = sorted({row[7] for row in members})
        result.append(
            {
                "id": (
                    f"person-{person_id}"
                    if person_id is not None
                    else f"face-{representative[0]}"
                ),
                "person_id": person_id,
                "label": next(
                    (row[4] for row in members if row[3] == person_id and row[4]),
                    "",
                ),
                "face_ids": [row[0] for row in members],
                "face_count": len(members),
                "photo_count": len(photos),
                "photos": photos,
                "albums": [
                    {"year": year, "album_id": album_id}
                    for year, album_id in albums
                ],
                "representative": {
                    "year": representative[5],
                    "album_id": representative[6],
                    "name": representative[7],
                    "crop": {
                        "x": representative[8],
                        "y": representative[9],
                        "width": representative[10],
                        "height": representative[11],
                    },
                },
            }
        )

    return result


def find_matching_person(
    embedding: list[float],
    known_faces: list[tuple[int, np.ndarray]],
    similarity_threshold: float,
) -> int | None:
    vector = np.asarray(embedding, dtype=float)
    norm = np.linalg.norm(vector)
    if norm == 0:
        return None
    vector /= norm

    best_match = None
    best_similarity = similarity_threshold
    for person_id, face_embedding in known_faces:
        face_norm = np.linalg.norm(face_embedding)
        if face_norm == 0 or face_embedding.size != vector.size:
            continue
        similarity = float(vector @ (face_embedding / face_norm))
        if similarity >= best_similarity:
            best_match = person_id
            best_similarity = similarity
    return best_match