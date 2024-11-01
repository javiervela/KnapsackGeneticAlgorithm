#!/bin/bash

URL="http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/mknap1.txt"

OUTPUT_DIR="data"
OUTPUT_FILE="$OUTPUT_DIR/mknap1.txt"

mkdir -p "$OUTPUT_DIR"

echo "Downloading $URL to $OUTPUT_FILE..."
curl -o "$OUTPUT_FILE" "$URL"

if [ -f "$OUTPUT_FILE" ]; then
	echo "Download completed successfully in $OUTPUT_DIR."
else
	echo "Download failed."
fi
