import unittest

from people import count_distinct_people, group_similar_embeddings


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


if __name__ == "__main__":
    unittest.main()