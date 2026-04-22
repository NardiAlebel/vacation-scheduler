# Vacation Scheduler – Deployment Documentation

## Part A: Cloud Deployment

---

### A.1 – Cloud Provider Justification

I selected **GitHub Pages** as the cloud service provider to deploy my Vacation Scheduler application. GitHub Pages is a free static hosting service built directly into GitHub, which made it the most practical and cost-effective option for this project.

My application is a native Android app, so the deployment goal was to make the compiled APK publicly accessible through a professional, stable URL. GitHub Pages accomplishes this by serving a landing page and the downloadable APK from a dedicated `gh-pages` branch in my repository. There are no server costs, no subscription fees, and no infrastructure to maintain — the service is fully managed by GitHub.

I also chose GitHub Pages because it integrates natively with GitHub Actions, which I used to automate the entire build and deployment process. Every time I push changes to the `main` branch, GitHub Actions automatically compiles the APK inside a Docker container and publishes the updated files to GitHub Pages without any manual steps on my end. This combination of free hosting and built-in CI/CD automation made GitHub Pages the right fit for my project.

Additionally, GitHub Pages provides a permanent public URL in the format `https://<username>.github.io/<repository>/`, which satisfies the requirement of having a live, accessible deployment URL to submit alongside the GitLab repository.

---

### A.2 – Container Image Implementation

To build my Android application in a consistent, reproducible environment, I implemented a Docker container image defined in a `Dockerfile` at the root of my project.

The container is built on top of an **Ubuntu 22.04** base image. I chose Ubuntu because it is a stable, widely supported Linux distribution that works well with the Android SDK and the command-line build tools. Inside the image, I installed **OpenJDK 17**, which is required by Android Gradle Plugin 9.0.0. After setting up Java, I installed the **Android command-line tools** by downloading them directly from Google's servers using `wget`. From there, I used the `sdkmanager` tool to install the SDK components my project needs:

- `platform-tools` — for Android device communication utilities like `adb`
- `platforms;android-36` — the Android platform libraries my app compiles against
- `build-tools;34.0.0` — the compilation tools such as `aapt`, `d8`, and `apksigner`

Once the environment was ready, the Dockerfile copies the project source code into the container, makes the Gradle wrapper executable, and runs `./gradlew assembleDebug` to produce the APK.

I also created a `docker-compose.yml` file to make local builds easier. Running `docker-compose up --build` builds the image and mounts an `output/` directory on my host machine, so the compiled APK is accessible without having to manually copy files out of the container. This allows any developer to reproduce the exact same build environment locally using Docker, regardless of their operating system.

For the cloud deployment pipeline, I used a GitHub Actions workflow (`.github/workflows/deploy.yml`) running on an Ubuntu environment with Java 17 and the Android SDK pre-installed. When a push to `main` triggers the pipeline, it compiles the APK using Gradle, then copies the APK and the `deploy/index.html` landing page into a deployment directory and publishes everything to GitHub Pages automatically.

---

### Deployment Files

| File | Description |
|------|-------------|
| `Dockerfile` | Defines the Android build container (Ubuntu 22.04, JDK 17, Android SDK) |
| `docker-compose.yml` | Runs the container locally and outputs the APK to `./output/` |
| `.github/workflows/deploy.yml` | GitHub Actions pipeline: builds APK, deploys to GitHub Pages |
| `deploy/index.html` | Public-facing landing page with APK download link |
| `deploy.sh` | Shell script for local build and packaging |

---

### Deployment Architecture

```
Developer pushes to main branch on GitHub
        │
        ▼
GitHub Actions triggers deploy.yml workflow
        │
        ▼
Ubuntu runner with Java 17 + Android SDK 35
        │
        ▼
./gradlew assembleDebug produces app-debug.apk
        │
        ▼
APK + index.html copied to gh-pages branch
        │
        ▼
GitHub Pages serves landing page at:
https://nardiAlebel.github.io/vacation-scheduler/

── Local builds ──────────────────────────────
Docker container (Dockerfile + docker-compose.yml)
provides a portable, reproducible build environment
for running the same Gradle build on any machine.
```
