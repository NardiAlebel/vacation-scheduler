#!/usr/bin/env bash
# deploy.sh – Local deployment script for Vacation Scheduler
# Builds the Android APK using Docker and opens the landing page in a browser.
#
# Prerequisites: Docker must be installed and running.
# Usage: bash deploy.sh

set -e

echo "================================================="
echo "  Vacation Scheduler – Local Deployment Script"
echo "================================================="
echo ""

# Step 1: Build the Docker image containing the Android SDK build environment
echo "[1/4] Building Docker image (Android SDK + Gradle)..."
docker build -t vacation-scheduler-build .

# Step 2: Run the container to compile and produce the debug APK
echo ""
echo "[2/4] Compiling APK inside Docker container..."
mkdir -p output
docker run --rm \
  -v "$(pwd)/output:/app/app/build/outputs/apk/debug" \
  vacation-scheduler-build \
  ./gradlew assembleDebug --no-daemon

# Step 3: Copy APK and landing page into the deploy directory
echo ""
echo "[3/4] Assembling deployment package..."
cp output/app-debug.apk deploy/VacationScheduler.apk

# Step 4: Report success and provide instructions
echo ""
echo "[4/4] Done!"
echo ""
echo "  APK location : deploy/VacationScheduler.apk"
echo "  Landing page : deploy/index.html"
echo ""
echo "To preview locally, open deploy/index.html in a browser."
echo "To deploy to GitHub Pages, push to the 'main' branch on GitHub."
echo "The GitHub Actions workflow (.github/workflows/deploy.yml) will"
echo "automatically build and publish to GitHub Pages on every push."
echo ""
echo "================================================="
