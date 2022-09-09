docker run -d --rm --name minio -p 9100:9000 -p 9101:9001 -v miniodata:/data \
quay.io/minio/minio server /data --console-address ":9001"