from pathlib import Path

from flask import Flask, jsonify, render_template
from flask_cors import CORS

app = Flask(__name__)
CORS(
    app,
    resources={
        r"/api/*": {
            "origins": [
                "https://are-you-alive-vjxi.onrender.com",
                "http://localhost:5000",
                "http://localhost:3000",
            ],
            "methods": ["GET", "POST", "OPTIONS"],
            "allow_headers": ["Content-Type"],
        }
    },
)


@app.route("/")
def index():
    index_template = Path(app.template_folder or "templates") / "index.html"
    if index_template.exists():
        return render_template("index.html")

    return jsonify({"status": "ok", "service": "Are You Alive"})


@app.route("/api/health")
def health():
    return jsonify({"status": "ok"})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
