export PATH="/home/smile/x86_64-linux-musl-cross/bin:$PATH"
OPENSSL_STATIC=1 OPENSSL_LIB_DIR=/usr/local/openssl/lib OPENSSL_INCLUDE_DIR=/usr/local/openssl/include cargo build --release --target=x86_64-unknown-linux-musl
