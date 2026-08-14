import json, os, shutil, sys

Q = ".commit-queue"
STATE = os.path.join(Q, "state.json")
MANIFEST = os.path.join(Q, "manifest.json")
MSG_OUT = "/tmp/commit_message.txt"
STATUS_OUT = "/tmp/commit_status.txt"

with open(STATE) as f:
    state = json.load(f)
with open(MANIFEST) as f:
    manifest = json.load(f)

idx = state["next_index"]

if idx >= len(manifest):
    with open(STATUS_OUT, "w") as f:
        f.write("skip")
    print("No more chunks queued.")
    sys.exit(0)

entry = manifest[idx]
chunk_dir = os.path.join(Q, "chunks", entry["chunk"])

for root, _, files in os.walk(chunk_dir):
    for fname in files:
        src = os.path.join(root, fname)
        rel = os.path.relpath(src, chunk_dir)
        dst = os.path.join(".", rel)
        os.makedirs(os.path.dirname(dst) or ".", exist_ok=True)
        shutil.copy2(src, dst)

shutil.rmtree(chunk_dir)

state["next_index"] = idx + 1
is_last = state["next_index"] >= len(manifest)

with open(STATE, "w") as f:
    json.dump(state, f, indent=2)

with open(MSG_OUT, "w") as f:
    f.write(entry["message"])

with open(STATUS_OUT, "w") as f:
    f.write("last" if is_last else "ok")

if is_last:
    shutil.rmtree(Q, ignore_errors=True)
    workflow_path = ".github/workflows/daily-build.yml"
    if os.path.exists(workflow_path):
        os.remove(workflow_path)

print("Applied chunk", entry["chunk"], "-", entry["message"])
