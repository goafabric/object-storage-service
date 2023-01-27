- you can bring up the application and databases seperatly inside src/deploy docker
- login to minio is on port 9101, minioadmin:minioadmin

# run minio
docker run --name s3minio --rm -p9100:9000 -p9101:9001 -v miniodata:/data quay.io/minio/minio:RELEASE.2023-01-25T00-19-54Z server /data --console-address ':9001'