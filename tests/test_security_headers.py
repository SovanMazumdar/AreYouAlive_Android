import unittest

from app import CONTENT_SECURITY_POLICY, app


class SecurityHeadersTest(unittest.TestCase):
    def setUp(self):
        self.client = app.test_client()

    def assert_security_headers(self, path):
        response = self.client.get(path)

        self.assertEqual(response.headers["Content-Security-Policy"], CONTENT_SECURITY_POLICY)
        self.assertEqual(response.headers["X-Content-Type-Options"], "nosniff")
        self.assertEqual(response.headers["X-Frame-Options"], "SAMEORIGIN")

    def test_index_includes_security_headers(self):
        self.assert_security_headers("/")

    def test_api_health_includes_security_headers(self):
        self.assert_security_headers("/api/health")


if __name__ == "__main__":
    unittest.main()
