openssl enc -aes-256-cbc -base64 -pbkdf2 -iter 1000 -salt -in .env.prod -out .env.prod.enc -pass "file:tools/enc_password_file"

