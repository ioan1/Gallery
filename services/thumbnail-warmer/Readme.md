# Thumbnail warmer

This Kubernetes CronJob calls the years, albums, and album-content APIs, then requests a thumbnail for each media file supported by the thumbnails service (`jpg`, `jpeg`, `heic`, `mp4`, and `mov`). Requests are sequential, with a one-second pause between files by default.

Build and push the image:

```sh
docker build -t docker.redby.fr/gallery/thumbnail-warmer:1.0.0 ./services/thumbnail-warmer
docker push docker.redby.fr/gallery/thumbnail-warmer:1.0.0
```

Create a dedicated Cognito user with a permanent password. Enable the `USER_PASSWORD_AUTH` flow (also called `ALLOW_USER_PASSWORD_AUTH`) for the app client. Do not use a temporary password that requires an interactive password change at first sign-in.

Set `COGNITO_USERNAME` and `COGNITO_PASSWORD` directly in the CronJob's `env` section. This stores the credentials in plain text in the manifest and makes them visible to users who can read the repository or pod specification. The job exchanges these credentials for a short-lived access token before calling the APIs. The client ID and region in the CronJob must match the Cognito app client used by the frontend.

Deploy or update the nightly job:

```sh
kubectl apply -f infra/services/thumbnail-warmer/cronjob.yaml
```

The schedule is 03:15 in the Kubernetes cluster's timezone. `WARM_INTERVAL_SECONDS` can be changed in the CronJob manifest to adjust the pause. To run it immediately, create a one-off Job with `kubectl create job --from=cronjob/thumbnail-warmer thumbnail-warmer-manual -n gallery`.