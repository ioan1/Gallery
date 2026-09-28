# Thumbnail warmer

This Kubernetes CronJob calls the years, albums, and album-content APIs, then requests a thumbnail for each media file supported by the thumbnails service (`jpg`, `jpeg`, `heic`, `mp4`, and `mov`). Requests are sequential, with a one-second pause between files by default.

Build and push the image:

```sh
docker build -t docker.redby.fr/gallery/thumbnail-warmer:1.0.0 ./services/thumbnail-warmer
docker push docker.redby.fr/gallery/thumbnail-warmer:1.0.0
```

Create the Cognito refresh-token Secret in the `gallery` namespace:

```sh
kubectl create secret generic thumbnail-warmer-auth \
  --namespace gallery \
  --from-literal=refresh-token='<COGNITO_REFRESH_TOKEN>' \
  --dry-run=client -o yaml | kubectl apply -f -
```

The job exchanges this refresh token for a short-lived access token before calling the APIs. The Cognito app client must allow the `REFRESH_TOKEN_AUTH` flow. Replace the refresh token before its configured expiration; the client ID and region in the CronJob must match the Cognito app client used by the frontend.

Deploy or update the nightly job:

```sh
kubectl apply -f infra/services/thumbnail-warmer/cronjob.yaml
```

The schedule is 03:15 in the Kubernetes cluster's timezone. `WARM_INTERVAL_SECONDS` can be changed in the CronJob manifest to adjust the pause. To run it immediately, create a one-off Job with `kubectl create job --from=cronjob/thumbnail-warmer thumbnail-warmer-manual -n gallery`.