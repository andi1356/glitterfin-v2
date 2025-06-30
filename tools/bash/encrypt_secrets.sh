openssl enc -aes-256-cbc -pbkdf2 -iter 1000 -salt -in secrets -out secrets.enc -pass "file:tools/enc_password_file"

