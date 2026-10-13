import unittest

import numpy as np

from people import (
    count_distinct_people,
    find_matching_person,
    group_face_rows,
    group_similar_embeddings,
)


class CountDistinctPeopleTests(unittest.TestCase):
    def test_groups_similar_embeddings(self):
        embeddings = ["[1,0]", "[0.98,0.2]", "[0,1]"]

        self.assertEqual(count_distinct_people(embeddings, 0.8), 2)

    def test_empty_embeddings_return_zero(self):
        self.assertEqual(count_distinct_people([], 0.45), 0)

    def test_groups_similar_embeddings_across_input_items(self):
        embeddings = ["[1,0]", "[0,1]", "[0.98,0.2]"]

        self.assertEqual(
            group_similar_embeddings(embeddings, 0.8),
            [[0, 2], [1]],
        )


class GroupFaceRowsTests(unittest.TestCase):
    def test_people_include_unique_photo_counts_and_are_ordered_by_photo_count(self):
        rows = [
            (1, "[1,0]", 0.9, 1, "Alice", "2024", "album", "a.jpg", 0, 0, 10, 10),
            (2, "[0.99,0.1]", 0.9, 1, "Alice", "2024", "album", "a.jpg", 0, 0, 10, 10),
            (3, "[0.98,0.2]", 0.9, 1, "Alice", "2024", "album", "b.jpg", 0, 0, 10, 10),
            (4, "[0,1]", 0.9, 2, "Bob", "2024", "album", "c.jpg", 0, 0, 10, 10),
        ]

        people = group_face_rows(rows, 0.8)

        self.assertEqual([person["label"] for person in people], ["Alice", "Bob"])
        self.assertEqual(people[0]["photo_count"], 2)
        self.assertEqual(people[0]["photos"], ["a.jpg", "b.jpg"])
        self.assertEqual(people[1]["photo_count"], 1)


class FindMatchingPersonTests(unittest.TestCase):
    def test_matches_against_each_face_assigned_to_a_person(self):
        known_faces = [
            (7, np.array([1.0, 0.0])),
            (7, np.array([0.0, 1.0])),
            (9, np.array([-1.0, 0.0])),
        ]

        self.assertEqual(
            find_matching_person([0.0, 0.98], known_faces, 0.8),
            7,
        )

    def test_returns_none_when_no_face_matches(self):
        known_faces = [(7, np.array([1.0, 0.0]))]

        self.assertIsNone(
            find_matching_person([0.0, 1.0], known_faces, 0.8),
        )


if __name__ == "__main__":
    unittest.main()