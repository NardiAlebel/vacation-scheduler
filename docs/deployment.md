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

The container is built on top of an **Ubuntu 22.04** base image. I chose Ubuntu because it is a stable, widely supported Linux distribution that works well with the Android SDK and the command-line build tools. Inside the image, I installed **OpenJDK 11**, which is required by the Gradle build system and matches the Java version configured in my `build.gradle.kts` file.

After setting up Java, I installed the **Android command-line tools** by downloading them directly from Google's servers using `wget`. From there, I used the `sdkmanager` tool to install the three SDK components my project needs:

- `platform-tools` — for Android device communication utilities like `adb`
- `platforms;android-36` — the Android 36 platform libraries my app compiles against
- `build-tools;34.0.0` — the compilation tools such as `aapt`, `d8`, and `apksigner`

Once the environment was ready, the Dockerfile copies the project source code into the container, makes the Gradle wrapper executable, and runs `./gradlew assembleDebug` to produce the APK.

I also created a `docker-compose.yml` file to make local builds easier. Running `docker-compose up --build` builds the image and mounts an `output/` directory on my host machine, so the compiled APK is accessible without having to manually copy files out of the container.

The Docker container is also used directly in my GitHub Actions workflow (`.github/workflows/deploy.yml`). When a push to `main` triggers the pipeline, GitHub Actions builds the Docker image, runs the container to compile the APK, then copies the APK and the `deploy/index.html` landing page into a deployment directory and publishes it to GitHub Pages. This means the same container that I use for local builds is the one producing the artifact that gets deployed to the cloud, ensuring the build output is always consistent regardless of where it is built.

---

### Deployment Files

| File | Description |
|------|-------------|
| `Dockerfile` | Defines the Android build container (Ubuntu 22.04, JDK 11, Android SDK 36) |
| `docker-compose.yml` | Runs the container locally and outputs the APK to `./output/` |
| `.github/workflows/deploy.yml` | GitHub Actions pipeline: builds APK in Docker, deploys to GitHub Pages |
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
Docker image built from Dockerfile
(Ubuntu 22.04 + JDK 11 + Android SDK 36)
        │
        ▼
Container runs: ./gradlew assembleDebug
        │
        ▼
app-debug.apk produced inside container
        │
        ▼
APK + index.html copied to gh-pages branch
        │
        ▼
GitHub Pages serves landing page at:
https://<username>.github.io/vacation-scheduler/
```
