export PATH="/home/smile/x86_64-linux-musl-cross/bin:$PATH"
OPENSSL_STATIC=1 OPENSSL_LIB_DIR=/usr/local/openssl/lib OPENSSL_INCLUDE_DIR=/usr/local/openssl/include cargo run --package vueblog-after --target=x86_64-unknown-linux-musl
