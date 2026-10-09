import unittest

import numpy as np

from people import count_distinct_people, find_matching_person, group_similar_embeddings


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